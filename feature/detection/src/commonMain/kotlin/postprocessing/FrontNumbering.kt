package postprocessing

import analyzer.IMAGE_TYPE_LOWER
import analyzer.IMAGE_TYPE_UPPER
import model.ToothBox
import kotlin.math.abs

object FrontNumbering {

    private fun defineNumberingInd(
        boxes: MutableList<ToothBox>,
        sortedIndsBoxes: List<Int>,
        sortedEachCls: Map<String, Map<String, List<Int>>>,
        imgType: String,
        checkupJawType: String = "front"
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
                            if (checkupJawType == "front")
                                frontLowerNumberingMapping[category]?.get(dir)?.get(ind)
                                    ?.let { numbering ->
                                        boxes[index].number =
                                            lowerNumberingInds[numbering]
                                                ?: -1
                                        boxes[index].alphabeticNumber = numbering
                                    }
                            else
                                lowerNumberingMapping[category]?.get(dir)?.get(ind)
                                    ?.let { numbering ->
                                        boxes[index].number =
                                            lowerNumberingInds[numbering]
                                                ?: -1
                                        boxes[index].alphabeticNumber = numbering
                                    }

                        }

                        "upper" -> {
                            upperNumberingMapping[category]?.get(dir)?.get(ind)?.let { numbering ->
                                boxes[index].number =
                                    upperNumberingInds[numbering]
                                        ?: -1
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

    private fun defineDir(
        sortedEachCls: Map<String, Map<String, List<Int>>>,
        cls: Int,
        ind: Int
    ): String {
        val locR = sortedEachCls[cls.toString()]?.get("r")?.indexOf(ind) ?: -1
        var dir = "r"
        if (locR == -1) {
            dir = "l"
            val locL = sortedEachCls[cls.toString()]?.get("l")?.indexOf(ind) ?: -1
        }
        return dir
    }

    private fun addingMissingTeethFront(
        teethCoordinate: Array<DoubleArray>,
        sortedPointsIndx: List<Int>,
        sortedEachCls: MutableMap<String, MutableMap<String, MutableList<Int>>>,
        clsInc: Int,
        minTh: Double = 0.024
    ): Pair<MutableMap<String, MutableMap<String, MutableList<Int>>>, Int> {
//        val teeth = teethCoordinate.copyOf()
        var numAllMissing = 0

        for (ind in 0 until sortedPointsIndx.size - 1) {
            val rect1 = teethCoordinate[sortedPointsIndx[ind]]
            val rect2 = teethCoordinate[sortedPointsIndx[ind + 1]]
            val rxRect1 = rect1[1] + (rect1[3] / 2)
            val lxRect2 = rect2[1] - (rect2[3] / 2)
            val distRects = lxRect2 - rxRect1

            if (distRects > minTh) {
                println("dist: $distRects")
                val cls1 = rect1[0].toInt()
                val cls2 = rect2[0].toInt()
                val dir1 = defineDir(sortedEachCls, cls1, sortedPointsIndx[ind])
                val dir2 = defineDir(sortedEachCls, cls2, sortedPointsIndx[ind + 1])
                val pos1 = sortedPointsIndx[ind]
                val pos2 = sortedPointsIndx[ind + 1]
                println("missing teeth:")
                println("$cls1 $cls2 $dir1 $dir2 $sortedEachCls")

                val (addMissing, _) = findAddMissingTeethFront(
                    clsInc,
                    cls1,
                    cls2,
                    dir1,
                    dir2,
                    1,
                    sortedEachCls,
                    pos1,
                    pos2
                )

                if (!addMissing) {
                    numAllMissing += 1
                }
            }
        }

        var numTeeth = 0
        for (key in sortedEachCls.keys) {
            numTeeth += sortedEachCls[key]?.get("l")?.size ?: 0
            numTeeth += sortedEachCls[key]?.get("r")?.size ?: 0
        }
        numTeeth += numAllMissing

        val clsMolar = (3 + clsInc).toString()
        if (clsMolar in sortedEachCls.keys) {
            sortedEachCls[clsMolar]?.get("r")?.let { right ->
                if (right.isNotEmpty() && right.size < 3) {
                    val toAdd = IntArray(3 - right.size) { -1 }.toList()
                    sortedEachCls[clsMolar]?.put("r", (toAdd + right).toMutableList())
                }
            }
        }

        val clsPremolar = (2 + clsInc).toString()
        if (clsPremolar in sortedEachCls.keys) {
            sortedEachCls[clsPremolar]?.get("r")?.let { right ->
                if (right.size == 1) {
                    sortedEachCls[clsPremolar]?.put("r", (listOf(-1) + right).toMutableList())
                }
            }
        }

        return sortedEachCls to numTeeth
    }


    fun numbering(
        boxes_: List<ToothBox>,
        imgType: String,
        subJawType: String = "front",
        normalizedPadding:Float
    ): NumberingResult {
        var boxes = boxes_.toMutableList()
        var sortedEachCls = mutableMapOf<String, MutableMap<String, MutableList<Int>>>()
        var numTeeth = 0

        val clsInc = if (imgType == "lower") 4 else 0

        if (boxes.isNotEmpty()) {
            val numIncisors = boxes.count { it.category == clsInc }
            var rightBoxes = mutableListOf<Int>()
            var leftBoxes = mutableListOf<Int>()
            var sortedIncisorInd = mutableListOf<Int>()
            var indIncisor = 0
            var leftSortedInd = mutableListOf<Int>()
            var rightSortedInd = mutableListOf<Int>()

            if (numIncisors > 0) {
                val incisorInd = boxes.indices.filter { boxes[it].category == clsInc }
                sortedIncisorInd = incisorInd.sortedBy { boxes[it].x }.toMutableList()

                val otherInd = boxes.indices.filter { !incisorInd.contains(it) }

                if (otherInd.isNotEmpty()) {
                    rightSortedInd = otherInd.filter { boxes[it].x <= boxes[sortedIncisorInd[0]].x }
                        .toMutableList()
                    val ignoreInd =
                        otherInd.filter { (boxes[it].x > boxes[sortedIncisorInd[0]].x) && (boxes[it].x < boxes[sortedIncisorInd.last()].x) }
                    for (indDel in ignoreInd) {
                        boxes[indDel].category = -1
                    }
                    leftSortedInd =
                        otherInd.filter { boxes[it].x >= boxes[sortedIncisorInd.last()].x }
                            .toMutableList()
                }

                for ((indI, i) in sortedIncisorInd.withIndex()) {
                    when {
                        numIncisors == 4 && indIncisor < 2 -> {
                            rightBoxes.add(i)
                            indIncisor += 1
                        }

                        numIncisors == 4 && indIncisor >= 2 -> {
                            leftBoxes.add(i)
                            indIncisor += 1
                        }

                        numIncisors == 3 && indI == 0 -> {
                            rightBoxes.add(i)
                            indIncisor += 1
                        }

                        numIncisors == 3 && indI == (sortedIncisorInd.size - 1) -> {
                            leftBoxes.add(i)
                            indIncisor += 1
                        }

                        rightSortedInd.isNotEmpty() -> {
                            if (abs(boxes[rightSortedInd.last()].x - boxes[i].x) < 0.4) {
                                rightBoxes.add(i)
                                indIncisor += 1
                            } else {
                                leftBoxes.add(i)
                                indIncisor += 1
                            }
                        }

                        leftSortedInd.isNotEmpty() -> {
                            if (abs(boxes[leftSortedInd[0]].x - boxes[i].x) < 0.4) {
                                leftBoxes.add(i)
                                indIncisor += 1
                            } else {
                                rightBoxes.add(i)
                                indIncisor += 1
                            }
                        }

                        boxes[i].x < 0.5 -> {
                            rightBoxes.add(i)
                            indIncisor += 1
                        }

                        else -> {
                            leftBoxes.add(i)
                        }
                    }
                }

                println("right boxes: $rightBoxes, left boxes: $leftBoxes")
                val ignoredResult =
                    UpperLowerNumbering.ignoreExtraBoxes(boxes, rightBoxes, 0, sortedIncisorInd)
                boxes = ignoredResult.first.toMutableList()
                val rightBoxesUpdated = ignoredResult.second.toMutableList()
                sortedIncisorInd = ignoredResult.third.toMutableList()
                val ignoredResultLeft =
                    UpperLowerNumbering.ignoreExtraBoxes(boxes, leftBoxes, 0, sortedIncisorInd)
                boxes = ignoredResultLeft.first.toMutableList()
                val leftBoxesUpdated = ignoredResultLeft.second.toMutableList()
                sortedIncisorInd = ignoredResultLeft.third.toMutableList()
                sortedEachCls[clsInc.toString()] =
                    mutableMapOf("r" to rightBoxesUpdated, "l" to leftBoxesUpdated)
                println("sorted_each_cls: $sortedEachCls")
            } else {
                rightSortedInd = boxes.indices.filter { boxes[it].x < 0.5 }.toMutableList()
                leftSortedInd = boxes.indices.filter { boxes[it].x >= 0.5 }.toMutableList()
            }

            // numbering
            if (rightSortedInd.isNotEmpty() || leftSortedInd.isNotEmpty()) {
                val numClasses = 4

                for (i in 1 until numClasses) {
                    var ind = i
                    if (imgType == "lower") {
                        ind += clsInc
                    }
                    if (leftSortedInd.isNotEmpty()) {
                        leftBoxes =
                            leftSortedInd.filter { boxes[it].category == ind }.toMutableList()
                        val ignoredResult = UpperLowerNumbering.ignoreExtraBoxes(
                            boxes,
                            leftBoxes,
                            ind - clsInc,
                            leftSortedInd
                        )
                        boxes = ignoredResult.first.toMutableList()
                        leftBoxes = ignoredResult.second.toMutableList()
                        leftSortedInd = ignoredResult.third.toMutableList()
                    }

                    if (rightSortedInd.isNotEmpty()) {
                        rightBoxes =
                            rightSortedInd.filter { boxes[it].category == ind }.toMutableList()
                        val ignoredResult = UpperLowerNumbering.ignoreExtraBoxes(
                            boxes,
                            rightBoxes,
                            ind - clsInc,
                            rightSortedInd
                        )
                        boxes = ignoredResult.first.toMutableList()
                        rightBoxes = ignoredResult.second.toMutableList()
                        rightSortedInd = ignoredResult.third.toMutableList()
                    }

                    sortedEachCls[ind.toString()] =
                        mutableMapOf("r" to rightBoxes, "l" to leftBoxes)
                }
                println("sorted_each_cls: $sortedEachCls")

                val sortedPointsIndex = rightSortedInd + sortedIncisorInd + leftSortedInd
                println("all_sorted_ind: $sortedPointsIndex")
                val result = addingMissingTeethFront(
                    boxes.exportTeethCoordinate(),
                    sortedPointsIndex,
                    sortedEachCls,
                    clsInc
                )
                sortedEachCls = result.first
                numTeeth = result.second

                if (sortedIncisorInd.isNotEmpty()) {
                    boxes = defineNumberingInd(
                        boxes,
                        sortedIncisorInd,
                        sortedEachCls,
                        imgType,
                    ).toMutableList()
                }

                boxes = defineNumberingInd(
                    boxes,
                    leftSortedInd,
                    sortedEachCls,
                    imgType,
                ).toMutableList()
                boxes = defineNumberingInd(
                    boxes,
                    rightSortedInd,
                    sortedEachCls,
                    imgType,
                ).toMutableList()
                boxes = boxes.filter { it.category != -1 }.toMutableList()
            } else { // if the class of whole boxes is incisor
                val result = addingMissingTeethFront(
                    boxes.exportTeethCoordinate(),
                    sortedIncisorInd,
                    sortedEachCls,
                    clsInc
                )
                sortedEachCls = result.first
                numTeeth = result.second

                boxes = defineNumberingInd(
                    boxes,
                    sortedIncisorInd,
                    sortedEachCls,
                    imgType,
                ).toMutableList()
                boxes = boxes.filter { it.category != -1 }.toMutableList()
            }
        }
        println("sorted_each_cls: $sortedEachCls")
        val missingTeeth = defineNumberingMissing(
            sortedEachCls = sortedEachCls,
            imgType = imgType,
            subJawType = subJawType
        )
        return NumberingResult(
            numTeeth = numTeeth,
            boxes = boxes,
            missing = missingTeeth,
            visibleBoxes = boxes.filterVisibleBoxes(normalizedPadding)
        )
    }

    fun separateUpperLower(boxes: List<ToothBox>): SeparateToothBoxes {
        val upperBoxesInd = boxes.indices.filter { it -> boxes[it].category in 0..3 }
        val upperBoxes = boxes.filter { it.category in 0..3 }
        val lowerBoxesInd = boxes.indices.filter { it -> boxes[it].category in 4..7 }
        val lowerBoxes = boxes.filter { it.category in 4..7 }
        return SeparateToothBoxes(upperBoxesInd, upperBoxes, lowerBoxesInd, lowerBoxes)
    }

    fun processNumberingForFront(
        normalizedToothBox: List<ToothBox>,
        normalizedPadding: Float
    ): NumberingResult {
        val separatedUpperAndLower =
            separateUpperLower(normalizedToothBox)
        val numberingResultForLower = FrontNumbering.numbering(
            boxes_ = separatedUpperAndLower.lowerBoxes,
            imgType = IMAGE_TYPE_LOWER,
            normalizedPadding = normalizedPadding
        )
        val numberingResultForUpper = FrontNumbering.numbering(
            boxes_ = separatedUpperAndLower.upperBoxes,
            imgType = IMAGE_TYPE_UPPER,
            normalizedPadding = normalizedPadding
        )

        if (numberingResultForUpper.boxes.isEmpty() || numberingResultForLower.boxes.isEmpty())
            return NumberingResult()

        val numTeeth = numberingResultForUpper.numTeeth + numberingResultForLower.numTeeth
        val boxes = mutableListOf<ToothBox>().apply {
            addAll(numberingResultForLower.boxes)
            addAll(numberingResultForUpper.boxes)
        }
        val missing = mutableListOf<String>().apply {
            addAll(numberingResultForLower.missing)
            addAll(numberingResultForUpper.missing)
        }
        val visibleBoxes = mutableListOf<ToothBox>().apply {
            addAll(numberingResultForLower.visibleBoxes)
            addAll(numberingResultForUpper.visibleBoxes)
        }
        return NumberingResult(numTeeth, boxes, missing, visibleBoxes)
    }

    data class SeparateToothBoxes(
        val upperBoxesInd: List<Int>,
        val upperBoxes: List<ToothBox>,
        val lowerBoxesInd: List<Int>,
        val lowerBoxes: List<ToothBox>
    )

}