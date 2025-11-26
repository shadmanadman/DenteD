package preprocessing


import kotlin.collections.emptyList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object UpperLowerNumbering {

    /**
     * - for lower jaw, y of teeth is higher than molar
     * - for upper jaw, y of teeth is lower than molar
     */
    private fun ignoreAnotherJaw(
        boxes: MutableList<ToothBox>,
        imgType: String
    ): MutableList<Int> {
        val ignoreInd = mutableListOf<Int>()
        val molarBoxes =
            boxes.filter { it.category == 3 }
        val premolarInd =
            boxes.indices.filter { boxes[it].category == 2 }
        val canineInd =
            boxes.indices.filter { boxes[it].category == 1 }
        val yminMolar = molarBoxes.minByOrNull { it.y }?.y ?: 0f
        val ymaxMolar = molarBoxes.maxByOrNull { it.y }?.y ?: 0f

        if (imgType == "upper") {
            ignoreInd.addAll(premolarInd.filter { boxes[it].y > yminMolar })
            ignoreInd.addAll(canineInd.filter { boxes[it].y > yminMolar })
        } else if (imgType == "lower") {
            ignoreInd.addAll(premolarInd.filter { boxes[it].y < ymaxMolar })
            ignoreInd.addAll(canineInd.filter { boxes[it].y < ymaxMolar })
        }

        println("ignoreInd: $ignoreInd")

        return ignoreInd
    }

    ///find missing teeth for upper or lower jaw using distance between two teeth
    fun determineNumMissing(distRects: Double, c: Double): Int {
        val numMissingTeeth: Int
        when {
            (distRects > (0.5 * c)) && (distRects <= (1.5 * c)) -> {
                numMissingTeeth = 1
                println("1 missing tooth")
            }

            (distRects > (1.5 * c)) && (distRects <= (2.5 * c)) -> {
                numMissingTeeth = 2
                println("2 missing teeth")
            }

            (distRects > (2.5 * c)) && (distRects <= (3.5 * c)) -> {
                numMissingTeeth = 3
                println("3 missing teeth")
            }

            else -> {
                numMissingTeeth = 4
                println("4 missing teeth")
            }
        }
        return numMissingTeeth
    }


    /**
    This function analyzes the distance between teeth and adds -1 as missing teeth to the related position in sorted_each_cls dictionary.

    Args:
    teeth_coordinate: A numpy array containing boxes.
    sorted_points_indx: A list of indices corresponding to the sorted teeth.
    sorted_each_cls: A dictionary containing sorted teeth by class.
    min_th: Minimum threshold distance to consider teeth missing (default 0.024).

    Returns:
    A tuple containing the updated sorted_each_cls dictionary and the total number of teeth.
     */
    private fun addingMissingTeethUpOrLow(
        teethCoordinate: Array<DoubleArray>,
        sortedPointsIndx: List<Int>,
        sortedEachCls: MutableMap<String, MutableMap<String, MutableList<Int>>>,
        minTh: Double = 0.024
    ): Pair<MutableMap<String, MutableMap<String, MutableList<Int>>>, Int> {
        var numAllMissing = 0

        for (ind in 0 until sortedPointsIndx.size - 1) {
            val rect1 = teethCoordinate[sortedPointsIndx[ind]]
            val rect2 = teethCoordinate[sortedPointsIndx[ind + 1]]
            val distRects1 = max(
                abs(rect1[1] - rect2[1]) - ((rect1[3] + rect2[3]) / 2),
                abs(rect1[2] - rect2[2]) - ((rect1[4] + rect2[4]) / 2)
            )
            val distRects = sqrt((rect1[1] - rect2[1]).pow(2) + (rect1[2] - rect2[2]).pow(2))
            val d1 = sqrt(rect1[3].pow(2) + rect1[4].pow(2))
            val d2 = sqrt(rect2[3].pow(2) + rect2[4].pow(2))
            val c = (d1 + d2) / 2

            if (distRects1 > minTh && distRects >= (0.5 * c)) {
                val numMissingTeeth =
                    determineNumMissing(distRects, c)
                val cls1 = rect1[0].toInt()
                val cls2 = rect2[0].toInt()
                val dir1 = defineDir(
                    sortedEachCls,
                    cls1,
                    sortedPointsIndx[ind]
                )
                val dir2 = defineDir(
                    sortedEachCls,
                    cls2,
                    sortedPointsIndx[ind + 1]
                )
                val pos1 = sortedPointsIndx[ind]
                val pos2 = sortedPointsIndx[ind + 1]

                val (addMissing, _) = findAddMissingTeeth(
                    cls1, cls2, dir1, dir2, numMissingTeeth, sortedEachCls, pos1, pos2
                )

                if (!addMissing) {
                    numAllMissing += numMissingTeeth
                }
            }
        }

        var numTeeth = 0
        sortedEachCls.keys.forEach { key ->
            numTeeth += sortedEachCls[key]!!["l"]!!.size + sortedEachCls[key]!!["r"]!!.size
        }
        numTeeth += numAllMissing

        // Ensure "3" exists in sortedEachCls and has at least 3 teeth
        sortedEachCls["3"]?.let { cls3 ->
            if (cls3.getOrDefault("r", emptyList()).isEmpty() || cls3.getOrDefault(
                    "r",
                    emptyList()
                ).size < 3
            ) {
                sortedEachCls["3"]?.set("r",
                    (MutableList(3 - (cls3.getOrDefault("r", emptyList()).size)) { -1 } + (cls3["r"]
                        ?: emptyList())).toMutableList())
            }
        }
//        // Check for key '3'
//        val class3 = sortedEachCls["3"]
//        class3?.get("r")?.let { r ->
//            if (r.isNotEmpty() && r.size < 3) {
//                numTeeth += (3 - r.size)
//                val additionalElements = List(3 - r.size) { -1 }
//                sortedEachCls["3"]?.set("r", (additionalElements + r).toMutableList())
//            }
//        }
//
//        // Check for key '2'
//        val class2 = sortedEachCls["2"]
//        class2?.get("r")?.let { r ->
//            if (r.size == 1) {
//                numTeeth += 1
//                sortedEachCls["2"]?.set("r", (listOf(-1) + r).toMutableList())
//            }
//        }

        return sortedEachCls to numTeeth
    }


    private fun defineDir(
        sortedEachCls: Map<String, Map<String, List<Int>>>,
        cls: Int,
        ind: Int
    ): String {
        val loc = mutableListOf<Int>()

        sortedEachCls[cls.toString()]?.get("r")?.let { rightIndices ->
            rightIndices.indexOf(ind).takeIf { it != -1 }?.let { index ->
                loc.add(index)
            }
        }

        var direction = "r"

        if (loc.isEmpty()) {
            direction = "l"
            sortedEachCls[cls.toString()]?.get("l")?.let { leftIndices ->
                leftIndices.indexOf(ind).takeIf { it != -1 }?.let { index ->
                    loc.add(index)
                }
            }
        }

        return direction
    }


    ///Number of indices to delete (minimum confidence)
    fun ignoreExtraBoxes(
        boxes: MutableList<ToothBox>,
        indices: List<Int>,
        indCls: Int,
        sortedIndices: MutableList<Int>
    ): Triple<MutableList<ToothBox>, List<Int>, List<Int>> {
        // Number of indices to delete (minimum confidence)
        /*
         Number of classes for each side of jaw:
         0 (incisors): each side (right or left) has maximum 2 incisors
         1 (canines): each side (right or left) has maximum 1 canines
         2 (premolars): each side (right or left) has maximum 2 premolars
         3 (molars): each side (right or left) has maximum 3 molars
         */
        val numEachClsSide = listOf(2, 1, 2, 3)
        val numIgnore = indices.size - numEachClsSide[indCls]
        if (numIgnore > 0) {
            // Find indices to delete with minimum confidence
            val indicesToDelete = boxes.asSequence().withIndex()
                .filter { indices.contains(it.index) }
                .sortedBy { it.value.maxConf }
                .take(numIgnore)
                .map { it.index }.toList()

            for (indDel in indicesToDelete) {
                boxes[indDel].category = -1
                val indexToRemove = sortedIndices.indexOf(indDel)
                if (indexToRemove != -1) {
                    sortedIndices.removeAt(indexToRemove)
                }
            }
            val remainingIndices = indices.filter { !indicesToDelete.contains(it) }
            return Triple(boxes, remainingIndices, sortedIndices)
        }
        return Triple(boxes, indices, sortedIndices)
    }


    private fun defineNumberingInd(
        boxes: MutableList<ToothBox>,
        sortedIndsBoxes: List<Int>,
        sortedEachCls: Map<String, Map<String, List<Int>>>,
        imgType: String
    ): MutableList<ToothBox> {
        for (index in sortedIndsBoxes) {
            val category =
                boxes[index].category.toString() // Convert category to string
            if (category != "-1") {
                // Define direction
                var loc: List<Int> = listOf()
                var dir = "r"
                sortedEachCls[category]?.get("r")?.let { rightIndices ->
                    rightIndices.indexOf(index).takeIf { it != -1 }?.let { rightIndex ->
                        loc = listOf(rightIndex)
                    }
                } ?: run {
                    dir = "l"
                    loc = sortedEachCls[category]?.get("l")
                        ?.mapIndexedNotNull { idx, value -> if (value == index) idx else null }
                        ?: emptyList()
                }
                loc.firstOrNull()?.let { ind ->
                    when (imgType) {
                        "lower" -> {
                            lowerNumberingMapping[category]?.get(dir)?.get(ind)?.let { numbering ->
                                boxes[index].number = lowerNumberingInds[numbering] ?: -1
                                boxes[index].alphabeticNumber = numbering
                            }

                        }

                        "upper" -> {
                            upperNumberingMapping[category]?.get(dir)?.get(ind)?.let { numbering ->
                                boxes[index].number = upperNumberingInds[numbering] ?: -1
                                boxes[index].alphabeticNumber = numbering
                            }
                        }

                        else -> {

                        }
                    }
                }
            }
        }
        return boxes
    }


    fun numberingV3(
        boxes: List<ToothBox>,
        imgType: String,
        normalizedPadding: Float
    ): NumberingResult {
        var mutableBoxes = boxes.toMutableList()
        val numIncisors =
            mutableBoxes.count { it.category == 0 }

        var rightBoxes = mutableListOf<Int>()
        var leftBoxes = mutableListOf<Int>()
        var sortedEachCls = mutableMapOf<String, MutableMap<String, MutableList<Int>>>()
        var indIncisor = 0
        var leftSortedInd = emptyList<Int>()
        var rightSortedInd = emptyList<Int>()
        var sortedIncisorInd = mutableListOf<Int>()
        val incisorIgnoreInd = mutableListOf<Int>()

        val incisorBoxes =
            mutableBoxes.filter { it.category == 0 }


        // if the photo has any box (tooth) with incisor class, sort boxes (teeth) with incisor classes and boxese with other classes (canine, premolar, molar) seperately. otherwise sort them after defining the direction of jaw (left or right)
        if (numIncisors > 0) {
            val incisorInd =
                mutableBoxes.indices.filter { mutableBoxes[it].category == 0 }
                    .toMutableList()

            var otherInd = mutableBoxes.indices.filter { incisorInd.contains(it).not() }
            val otherBoxes = otherInd.map { mutableBoxes[it] }

            println("incisor_boxes:$incisorBoxes \n other_boxes:$otherBoxes")

            val molarBoxes =
                otherBoxes.filter { it.category == 2 || it.category == 3 }


            val yminMolar = molarBoxes.minByOrNull { it.y }?.y ?: 0f
            val ymaxMolar = molarBoxes.maxByOrNull { it.y }?.y ?: 0f

            if (imgType == "upper") {
                incisorIgnoreInd.addAll(incisorInd.filter { mutableBoxes[it].y > yminMolar })
            } else if (imgType == "lower") {
                incisorIgnoreInd.addAll(incisorInd.filter { mutableBoxes[it].y < ymaxMolar })

            }

            println("incisorIgnoreInd: $incisorIgnoreInd")

            // Removing elements present in incisorIgnoreInd from incisorInd
            incisorInd.removeAll(incisorIgnoreInd)
            for (indDel in incisorIgnoreInd) {
                mutableBoxes[indDel].category = -1
            }

            sortedIncisorInd = incisorInd.sortedBy { mutableBoxes[it].x }.toMutableList()
            println("sorted_incisor_ind:$sortedIncisorInd")

            // #ignore teeth with canine and premolar classes from another jaw
            var ignoreInds = ignoreAnotherJaw(mutableBoxes, imgType)
            for (indDel in ignoreInds) {
                mutableBoxes[indDel].category = -1
            }

            otherInd = otherInd.filter { mutableBoxes[it].category != -1 }

            val boxesNoIgnore: List<ToothBox>
            val boxesNoIgnoreInd: List<Int>


            if (sortedIncisorInd.isNotEmpty()) {
                if (otherInd.isNotEmpty()) {
                    val rightInd =
                        otherInd.filter { mutableBoxes[it].x <= mutableBoxes[sortedIncisorInd[0]].x }
                    rightSortedInd = if (imgType == "upper") {
                        rightInd.sortedByDescending { mutableBoxes[it].y }
                    } else {
                        rightInd.sortedBy { mutableBoxes[it].y }
                    }
                    println("right_sorted_ind: $rightSortedInd")


                    val ignoreInd =
                        otherInd.filter { mutableBoxes[it].x > mutableBoxes[sortedIncisorInd[0]].x && mutableBoxes[it].x < mutableBoxes[sortedIncisorInd.last()].x }
                    ignoreInd.forEach { indDel ->
                        mutableBoxes[indDel].category = -1
                    }

                    val leftInd =
                        otherInd.filter { mutableBoxes[it].x >= mutableBoxes[sortedIncisorInd.last()].x }
                    leftSortedInd = if (imgType == "upper") {
                        leftInd.sortedBy { mutableBoxes[it].y }
                    } else {
                        leftInd.sortedByDescending { mutableBoxes[it].y }
                    }
                    println("left_sorted_ind: $leftSortedInd")
                }



                for ((indI, i) in sortedIncisorInd.withIndex()) {
                    when {
                        numIncisors == 4 && indIncisor < 2 -> {
                            rightBoxes.add(i)
                            indIncisor++
                            println("rule 1")
                        }

                        numIncisors == 4 && indIncisor >= 2 -> {
                            leftBoxes.add(i)
                            indIncisor++
                            println("rule 2")
                        }

                        numIncisors == 3 && indI == 0 -> {
                            rightBoxes.add(i)
                            indIncisor++
                            println("rule 3_first")
                        }

                        numIncisors == 3 && indI == sortedIncisorInd.size - 1 -> {
                            leftBoxes.add(i)
                            indIncisor++
                            println("rule 3_end")
                        }

                        rightSortedInd.isNotEmpty() -> {
                            // Here the box were required int but rightSortedInd.last() was float so i convert it to int
                            if (abs(
                                    mutableBoxes[rightSortedInd.last()
                                        .toInt()].x - mutableBoxes[i].x
                                ) < 0.4
                            ) {
                                rightBoxes.add(i)
                                indIncisor++
                                println("rule 3_1")
                            } else {
                                leftBoxes.add(i)
                                indIncisor++
                                println("rule 3_2")
                            }
                        }

                        leftSortedInd.isNotEmpty() -> {
                            if (abs(
                                    mutableBoxes[leftSortedInd.first()
                                        .toInt()].x - mutableBoxes[i].x
                                ) < 0.4
                            ) {
                                leftBoxes.add(i)
                                indIncisor++
                                println("rule 4_1")
                            } else {
                                rightBoxes.add(i)
                                indIncisor++
                                println("rule 4_2")
                            }
                        }

                        indI < sortedIncisorInd.size - 1 -> {
                            when {
                                imgType == "lower" && mutableBoxes[sortedIncisorInd[indI + 1]].y > mutableBoxes[i].y -> {
                                    rightBoxes.add(i)
                                    indIncisor++
                                    println("rule 8")
                                }

                                imgType == "lower" && mutableBoxes[sortedIncisorInd[indI + 1]].y <= mutableBoxes[i].y -> {
                                    leftBoxes.add(i)
                                    indIncisor++
                                    println("rule 9")
                                }

                                imgType == "upper" && mutableBoxes[sortedIncisorInd[indI + 1]].y < mutableBoxes[i].y -> {
                                    rightBoxes.add(i)
                                    indIncisor++
                                    println("rule 10")
                                }

                                imgType == "upper" && mutableBoxes[sortedIncisorInd[indI + 1]].y >= mutableBoxes[i].y -> {
                                    leftBoxes.add(i)
                                    indIncisor++
                                    println("rule 11")
                                }
                            }
                        }

                        indI != 0 && indI == sortedIncisorInd.size - 1 -> {
                            when {
                                imgType == "lower" && mutableBoxes[sortedIncisorInd[indI - 1]].y < mutableBoxes[i].y -> {
                                    rightBoxes.add(i)
                                    indIncisor++
                                    println("rule 8_2")
                                }

                                imgType == "lower" && mutableBoxes[sortedIncisorInd[indI - 1]].y >= mutableBoxes[i].y -> {
                                    leftBoxes.add(i)
                                    indIncisor++
                                    println("rule 9_2")
                                }

                                imgType == "upper" && mutableBoxes[sortedIncisorInd[indI - 1]].y > mutableBoxes[i].y -> {
                                    rightBoxes.add(i)
                                    indIncisor++
                                    println("rule 10_2")
                                }

                                imgType == "upper" && mutableBoxes[sortedIncisorInd[indI - 1]].y <= mutableBoxes[i].y -> {
                                    leftBoxes.add(i)
                                    indIncisor++
                                    println("rule 11_2")
                                }
                            }
                        }

                        indI == 0 -> {
                            leftBoxes.add(i)
                            println("just one incisor")
                        }
                    }
                }

                println("right boxes: $rightBoxes, left boxes: $leftBoxes")
                val result1 = ignoreExtraBoxes(mutableBoxes, rightBoxes, 0, sortedIncisorInd)
                mutableBoxes = result1.first
                rightBoxes = result1.second.toMutableList()
                sortedIncisorInd = result1.third.toMutableList()

                val result2 = ignoreExtraBoxes(mutableBoxes, leftBoxes, 0, sortedIncisorInd)
                mutableBoxes = result2.first
                leftBoxes = result2.second.toMutableList()
                sortedIncisorInd = result2.third.toMutableList()

                sortedEachCls["0"] = mapOf(
                    "r" to rightBoxes,
                    "l" to leftBoxes
                ).toMutableMap()
                println("sorted_each_cls: $sortedEachCls")

            } else {
                ignoreInds = ignoreAnotherJaw(mutableBoxes, imgType)
                for (indDel in ignoreInds) {
                    mutableBoxes[indDel].category = -1
                }

                boxesNoIgnore = mutableBoxes.filter { it.category != -1 }
                boxesNoIgnoreInd =
                    mutableBoxes.indices.filter { mutableBoxes[it].category != -1 }
                val yXmin = boxesNoIgnore.minByOrNull { it.x }?.y ?: 0f
                val yXmax = boxesNoIgnore.maxByOrNull { it.x }?.y ?: 0f

                when {
                    imgType == "upper" && yXmax < yXmin -> {
                        rightSortedInd = boxesNoIgnore.withIndex().sortedByDescending { it.value.y }
                            .map { boxesNoIgnoreInd[it.index] }
                    }

                    imgType == "upper" && yXmax >= yXmin -> {
                        leftSortedInd = boxesNoIgnore.withIndex().sortedBy { it.value.y }
                            .map { boxesNoIgnoreInd[it.index] }
                    }

                    imgType == "lower" && yXmax > yXmin -> {
                        rightSortedInd = boxesNoIgnore.withIndex().sortedBy { it.value.y }
                            .map { boxesNoIgnoreInd[it.index] }
                    }

                    imgType == "lower" && yXmax <= yXmin -> {
                        leftSortedInd = boxesNoIgnore.withIndex().sortedByDescending { it.value.y }
                            .map { boxesNoIgnoreInd[it.index] }
                    }
                }
            }
        } else {
            val yXmin = mutableBoxes.minByOrNull { it.x }?.y
                ?: 0f // Find a box with minimum x in boxes and return its y
            val yXmax = mutableBoxes.maxByOrNull { it.x }?.y
                ?: 0f // Find a box with maximum x in boxes_ and return its y

            when {
                imgType == "upper" && yXmax < yXmin -> { // right boxes_
                    val sortedWithIndex = mutableBoxes.withIndex().sortedByDescending { it.value.y }
                    rightSortedInd = sortedWithIndex.map { it.index }.toMutableList()
                }

                imgType == "upper" && yXmax >= yXmin -> { // left boxes
                    val sortedWithIndex = mutableBoxes.withIndex().sortedBy { it.value.y }
                    leftSortedInd = sortedWithIndex.map { it.index }.toMutableList()
                }

                imgType == "lower" && yXmax > yXmin -> { // right boxes
                    val sortedWithIndex = mutableBoxes.withIndex().sortedBy { it.value.y }
                    rightSortedInd = sortedWithIndex.map { it.index }.toMutableList()
                }

                imgType == "lower" && yXmax <= yXmin -> { // left boxes
                    val sortedWithIndex = mutableBoxes.withIndex().sortedByDescending { it.value.y }
                    leftSortedInd = sortedWithIndex.map { it.index }.toMutableList()
                }
            }
        }


        val numClasses = 4
        val numTeeth: Int

        if (rightSortedInd.isNotEmpty() || leftSortedInd.isNotEmpty()) {
            for (ind in 1 until numClasses) {

                if (leftSortedInd.isNotEmpty()) {
                    leftBoxes = leftSortedInd.filter { mutableBoxes[it].category == ind }
                        .toMutableList()
                    val result = ignoreExtraBoxes(
                        mutableBoxes,
                        leftBoxes,
                        ind,
                        leftSortedInd.toMutableList()
                    )
                    mutableBoxes = result.first
                    leftBoxes = result.second.toMutableList()
                    leftSortedInd = result.third
                }

                if (rightSortedInd.isNotEmpty()) {
                    rightBoxes = rightSortedInd.filter { mutableBoxes[it].category == ind }
                        .toMutableList()
                    val result = ignoreExtraBoxes(
                        mutableBoxes,
                        rightBoxes,
                        ind,
                        rightSortedInd.toMutableList()
                    )
                    mutableBoxes = result.first
                    rightBoxes = result.second.toMutableList()
                    rightSortedInd = result.third
                }

                sortedEachCls[ind.toString()] = mapOf(
                    "r" to rightBoxes,
                    "l" to leftBoxes
                ) as MutableMap<String, MutableList<Int>>
            }

            println("sorted_each_cls: $sortedEachCls")

            val sortedPointsIndex = rightSortedInd + sortedIncisorInd + leftSortedInd
            println("all_sorted_ind: $sortedPointsIndex")

            val result = addingMissingTeethUpOrLow(
                mutableBoxes.exportTeethCoordinate(),
                sortedPointsIndex,
                sortedEachCls
            )
            sortedEachCls = result.first
            numTeeth = result.second

            println("sorted_each_cls after adding missing teeth: $sortedEachCls")

            if (sortedIncisorInd.isNotEmpty()) {
                mutableBoxes =
                    defineNumberingInd(
                        mutableBoxes,
                        sortedIncisorInd,
                        sortedEachCls,
                        imgType
                    )
            }
            mutableBoxes = defineNumberingInd(
                mutableBoxes,
                leftSortedInd,
                sortedEachCls,
                imgType
            )
            mutableBoxes = defineNumberingInd(
                mutableBoxes,
                rightSortedInd,
                sortedEachCls,
                imgType
            )
            mutableBoxes.removeAll { it.category == -1 }
            // If the class of whole boxes is incisor
            // Considering missing teeth
        } else {
            val result = addingMissingTeethUpOrLow(
                mutableBoxes.exportTeethCoordinate(),
                sortedIncisorInd,
                sortedEachCls
            )
            sortedEachCls = result.first
            numTeeth = result.second

            mutableBoxes =
                defineNumberingInd(
                    mutableBoxes,
                    sortedIncisorInd,
                    sortedEachCls,
                    imgType
                )
            mutableBoxes.removeAll { it.category == -1 }
        }
        println("number of teeth:$numTeeth")
        val missingTeeth = defineNumberingMissing(
            sortedEachCls = sortedEachCls,
            imgType = imgType,
            subJawType = imgType
        )
        return NumberingResult(
            numTeeth,
            mutableBoxes,
            missingTeeth,
            mutableBoxes.filterVisibleBoxes(normalizedPadding)
        )
    }


}

data class NumberingResult(
    val numTeeth: Int = 0,
    val boxes: List<ToothBox> = emptyList(),
    val missing: List<String> = emptyList(),
    val visibleBoxes: List<ToothBox> = emptyList()
)