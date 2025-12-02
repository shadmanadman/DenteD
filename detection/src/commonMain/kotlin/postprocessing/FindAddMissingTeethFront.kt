package postprocessing

import kotlin.math.max
import kotlin.math.min

/**
 * This function specializes in finding and adding placeholders for missing front teeth (incisors, canines, etc.).
 * The logic for front teeth is significantly more complex than for posterior teeth due to two main reasons:
 * 1.Symmetry around the midline: The central incisors (class 0) are a left/right pair. Determining
 *     which one is missing when only one is present requires checking adjacent teeth.
 * 2.Common patterns of missing teeth: There are predictable anatomical patterns for which teeth are
 *     missing between two detected teeth (e.g., if a central incisor and a canine are present, the
 *     lateral incisor between them is likely missing).
 *
 * This function encapsulates all the specific rules for handling these complex scenarios for both the upper
 * (`clsInc = 0`) and lower (`clsInc = 8`) jaw, making the main processing logic cleaner and more maintainable.
 *
 *
 * This function finds the missing teeth position between two teeth with the class of cls1 and the class of cls2
 * and adds -1 for each missing tooth to the related position of sortedEachCls dictionary.
 * 
 *
 * @param cls1, cls2: Class labels of the two teeth being compared.
 * @param dir1, dir2: Directions ("l" or "r") of the two teeth in sortedEachCls.
 * @param sortedEachCls: The dictionary containing sorted teeth by class.
 * @param pos1, pos2: positions of the two teeth in boxes list
 *
 * @return addMissing: True if missing teeth is added to sortedEachCls, False otherwise.
 * @return sortedEachCls: updated sortedEachCls dictionary (by adding -1 for each missing tooth to the related position).
 */
fun findAddMissingTeethFront(
    clsInc: Int,
    cls1: Int,
    cls2: Int,
    dir1: String,
    dir2: String,
    numMissingTeeth: Int,
    sortedEachCls: MutableMap<String, MutableMap<String, MutableList<Int>>>,
    pos1: Int,
    pos2: Int
): Pair<Boolean, MutableMap<String, MutableMap<String, MutableList<Int>>>> {

    var addMissing = false
    var numMissingTeeth_ = numMissingTeeth

    if (cls1 == 0 + clsInc && cls2 == 0 + clsInc) {
        if (dir1 == "r" && dir2 == "l") {
            if ((sortedEachCls["${0 + clsInc}"]?.get("l")?.size ?: 0) < 2) {
                sortedEachCls["${0 + clsInc}"]?.get("l")?.add(0, -1)
                addMissing = true
            }
        }
    } else if (cls1 == 0 + clsInc && cls2 == 1 + clsInc) {
        if (dir1 == "r" && dir2 == "l") {
            when (sortedEachCls["${0 + clsInc}"]?.get("l")?.size ?: 0) {
                0 -> {
                    sortedEachCls["${0 + clsInc}"]?.get("l")?.addAll(listOf(-1, -1))
                    addMissing = true
                }

                1 -> {
                    sortedEachCls["${0 + clsInc}"]?.get("l")?.add(0, -1)
                    addMissing = true
                }
            }
        } else if (dir1 == "l" && dir2 == "l") {
            if ((sortedEachCls["${0 + clsInc}"]?.get("l")?.size ?: 0) < 2) {
                sortedEachCls["${0 + clsInc}"]?.get("l")?.add(-1)
                addMissing = true
            }
        }
    } else if (cls1 == 1 + clsInc && cls2 == 0 + clsInc) {
        if (dir1 == "r" && dir2 == "l") {
            when (sortedEachCls["${0 + clsInc}"]?.get("r")?.size ?: 0) {
                0 -> {
                    sortedEachCls["${0 + clsInc}"]?.get("r")?.addAll(listOf(-1, -1))
                    addMissing = true
                }

                1 -> {
                    sortedEachCls["${0 + clsInc}"]?.get("r")?.add(0, -1)
                    addMissing = true
                }
            }
        } else if (dir1 == "r" && dir2 == "r") {
            if ((sortedEachCls["${0 + clsInc}"]?.get("r")?.size ?: 0) < 2) {
                sortedEachCls["${0 + clsInc}"]?.get("r")?.add(0, -1)
                addMissing = true
            }
        }
    } else if (cls1 == 0 + clsInc && cls2 == 2 + clsInc) {
        if (sortedEachCls["${1 + clsInc}"]?.get("l")?.isEmpty() == true) {
            sortedEachCls["${1 + clsInc}"]?.get("l")?.add(-1)
            addMissing = true
        }
        if (dir1 == "r" && dir2 == "l") {
            when (sortedEachCls["${0 + clsInc}"]?.get("l")?.size ?: 0) {
                0 -> {
                    sortedEachCls["${0 + clsInc}"]?.get("l")?.addAll(listOf(-1, -1))
                    addMissing = true
                }

                1 -> {
                    sortedEachCls["${0 + clsInc}"]?.get("l")?.add(-1)
                    addMissing = true
                }
            }
        }
    } else if (cls1 == 2 + clsInc && cls2 == 0 + clsInc) {
        if (sortedEachCls["${1 + clsInc}"]?.get("r")?.isEmpty() == true) {
            sortedEachCls["${1 + clsInc}"]?.get("r")?.add(-1)
            addMissing = true
        }
        if (dir1 == "r" && dir2 == "r") {
            if (numMissingTeeth_ == 2) {
                if ((sortedEachCls["${0 + clsInc}"]?.get("r")?.size ?: 0) < 2) {
                    sortedEachCls["${0 + clsInc}"]?.get("r")?.add(0, -1)
                    addMissing = true
                }
            } else if (dir1 == "r" && dir2 == "l") {
                when (sortedEachCls["${0 + clsInc}"]?.get("r")?.size ?: 0) {
                    0 -> {
                        sortedEachCls["${0 + clsInc}"]?.get("r")?.addAll(listOf(-1, -1))
                        addMissing = true
                    }

                    1 -> {
                        sortedEachCls["${0 + clsInc}"]?.get("r")?.add(-1)
                        addMissing = true
                    }
                }
            }
        }
    } else if (cls1 == 0 + clsInc && cls2 == 3 + clsInc) {
        if (dir1 == "l" && dir2 == "l") {
            if (sortedEachCls["${2 + clsInc}"]?.get("l")?.size == 0) {
                sortedEachCls["${2 + clsInc}"]?.get("l")?.addAll(listOf(-1, -1))
                addMissing = true
            } else if (sortedEachCls["${2 + clsInc}"]?.get("l")?.size == 1) {
                sortedEachCls["${2 + clsInc}"]?.get("l")?.add(-1)
                addMissing = true
            }
            if (sortedEachCls["${1 + clsInc}"]?.get("l")?.size == 0) {
                sortedEachCls["${1 + clsInc}"]?.get("l")?.add(-1)
                addMissing = true
            }
            if (numMissingTeeth_ == 4) {
                if ((sortedEachCls["${3 + clsInc}"]?.get("l")?.size ?: 0) < 3) {
                    sortedEachCls["${3 + clsInc}"]?.get("l")?.add(0, -1)
                    addMissing = true
                }
            }
        }
    } else if (cls1 == 3 + clsInc && cls2 == 0 + clsInc) {
        if (dir1 == "r" && dir2 == "r") {
            if (sortedEachCls["${2 + clsInc}"]?.get("r")?.size == 0) {
                sortedEachCls["${2 + clsInc}"]?.get("r")?.addAll(listOf(-1, -1))
                addMissing = true
            } else if (sortedEachCls["${2 + clsInc}"]?.get("r")?.size == 1) {
                sortedEachCls["${2 + clsInc}"]?.get("r")?.add(-1)
                addMissing = true
            }
            if (sortedEachCls["${1 + clsInc}"]?.get("r")?.size == 0) {
                sortedEachCls["${1 + clsInc}"]?.get("r")?.add(-1)
                addMissing = true
            }
        }
    } else if (cls1 == 1 + clsInc && cls2 == 1 + clsInc) {
        if (sortedEachCls["${0 + clsInc}"]?.get("r")?.size == 0) {
            sortedEachCls["${0 + clsInc}"]?.get("r")?.addAll(listOf(-1, -1))
            addMissing = true
        } else if (sortedEachCls["${0 + clsInc}"]?.get("r")?.size == 1) {
            sortedEachCls["${0 + clsInc}"]?.get("r")?.add(-1)
            addMissing = true
        }
        if (sortedEachCls["${0 + clsInc}"]?.get("l")?.size == 0) {
            sortedEachCls["${0 + clsInc}"]?.get("l")?.addAll(listOf(-1, -1))
            addMissing = true
        } else if (sortedEachCls["${0 + clsInc}"]?.get("l")?.size == 1) {
            sortedEachCls["${0 + clsInc}"]?.get("l")?.add(-1)
            addMissing = true
        }
    } else if (cls1 == 1 + clsInc && cls2 == 2 + clsInc) {
        if (dir1 == "l" && dir2 == "l") {
            numMissingTeeth_ = 1
            if ((sortedEachCls["${2 + clsInc}"]?.get("l")?.size ?: 0) < 2) {
                sortedEachCls["${2 + clsInc}"]?.get("l")?.add(0, -1)
                addMissing = true
            }
        }
    } else if (cls1 == 2 + clsInc && cls2 == 1 + clsInc) {
        if (dir1 == "r" && dir2 == "r") {
            numMissingTeeth_ = 1
            if ((sortedEachCls["${2 + clsInc}"]?.get("r")?.size ?: 0) < 2) {
                sortedEachCls["${2 + clsInc}"]?.get("r")?.add(-1)
                addMissing = true
            }
        }
    } else if (cls1 == 1 + clsInc && cls2 == 3 + clsInc) {
        if (dir1 == "l" && dir2 == "l") {
            numMissingTeeth_ = max(numMissingTeeth_, 2)
            when (sortedEachCls["${2 + clsInc}"]?.get("l")?.size ?: 0) {
                0 -> {
                    sortedEachCls["${2 + clsInc}"]?.get("l")?.addAll(listOf(-1, -1))
                    addMissing = true
                }

                1 -> {
                    sortedEachCls["${2 + clsInc}"]?.get("l")?.add(-1)
                    addMissing = true
                }
            }
        }
    } else if (cls1 == 3 + clsInc && cls2 == 1 + clsInc) {
        if (dir1 == "r" && dir2 == "r") {
            if ((sortedEachCls["${2 + clsInc}"]?.get("r")?.size ?: 0) == 0) {
                sortedEachCls["${2 + clsInc}"]?.get("r")?.addAll(listOf(-1, -1))
                addMissing = true
            } else if ((sortedEachCls["${2 + clsInc}"]?.get("r")?.size ?: 0) == 1) {
                sortedEachCls["${2 + clsInc}"]?.get("r")?.add(-1)
                addMissing = true
            }
            numMissingTeeth_ = max(numMissingTeeth_, 2)
        }
    } else if (cls1 == 2 + clsInc && cls2 == 3 + clsInc) {
        if (dir1 == "l" && dir2 == "l") {
            numMissingTeeth_ = min(numMissingTeeth_, 3)
            if ((sortedEachCls["${2 + clsInc}"]?.get("l")?.size ?: 0) == 2) {
                if ((sortedEachCls["${3 + clsInc}"]?.get("l")?.size
                        ?: 0) < 2 && numMissingTeeth_ == 2
                ) {
                    sortedEachCls["${3 + clsInc}"]?.get("l")?.addAll(0, listOf(-1, -1))
                    addMissing = true
                } else if ((sortedEachCls["${3 + clsInc}"]?.get("l")?.size ?: 0) < 3) {
                    sortedEachCls["${3 + clsInc}"]?.get("l")?.add(0, -1)
                    addMissing = true
                }
            } else {
                if ((sortedEachCls["${2 + clsInc}"]?.get("l")?.size ?: 0) < 2) {
                    sortedEachCls["${2 + clsInc}"]?.get("l")?.add(-1)
                    addMissing = true
                }
                if (numMissingTeeth_ == 2 && (sortedEachCls["${3 + clsInc}"]?.get("l")?.size
                        ?: 0) < 3
                ) {
                    sortedEachCls["${3 + clsInc}"]?.get("l")?.add(0, -1)
                    addMissing = true
                }
            }
        }
    } else if (cls1 == 3 + clsInc && cls2 == 2 + clsInc) {
        if (dir1 == "r" && dir2 == "r") {
            numMissingTeeth_ = min(numMissingTeeth_, 3)
            if ((sortedEachCls["${2 + clsInc}"]?.get("r")?.size ?: 0) == 2) {
                if ((sortedEachCls["${3 + clsInc}"]?.get("r")?.size
                        ?: 0) < 2 && numMissingTeeth_ == 2
                ) {
                    sortedEachCls["${3 + clsInc}"]?.get("r")?.addAll(listOf(-1, -1))
                    addMissing = true
                } else if ((sortedEachCls["${3 + clsInc}"]?.get("r")?.size ?: 0) < 3) {
                    sortedEachCls["${3 + clsInc}"]?.get("r")?.add(-1)
                    addMissing = true
                }
            } else {
                if ((sortedEachCls["${2 + clsInc}"]?.get("r")?.size ?: 0) < 2) {
                    sortedEachCls["${2 + clsInc}"]?.get("r")?.add(-1)
                    addMissing = true
                }
                if (numMissingTeeth_ == 2 && (sortedEachCls["${3 + clsInc}"]?.get("r")?.size
                        ?: 0) < 3
                ) {
                    sortedEachCls["${3 + clsInc}"]?.get("r")?.add(-1)
                    addMissing = true
                }
            }
        }
    } else if (cls1 == 3 + clsInc && cls2 == 3 + clsInc) {
        numMissingTeeth_ = 1
        if (dir1 == "r" && dir2 == "r") {
            if ((sortedEachCls["${3 + clsInc}"]?.get("r")?.size ?: 0) == 2) {
                sortedEachCls["${3 + clsInc}"]?.get("r")?.apply {
                    clear()
                    add(pos1)
                    add(-1)
                    add(pos2)
                }
                addMissing = true
            }
        } else if (dir1 == "l" && dir2 == "l") {
            if ((sortedEachCls["${3 + clsInc}"]?.get("l")?.size ?: 0) == 2) {
                sortedEachCls["${3 + clsInc}"]?.get("l")?.apply {
                    clear()
                    add(pos1)
                    add(-1)
                    add(pos2)
                }
                addMissing = true
            }
        }
    }

    return Pair(addMissing, sortedEachCls)
}