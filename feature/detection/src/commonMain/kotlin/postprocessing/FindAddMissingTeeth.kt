package postprocessing


/**
 * Adds placeholders for missing teeth based on specific rules determined by tooth classes and positions.
 *
 * This function is a complex rule engine that modifies a map of teeth (`sortedEachCls`)
 * by inserting placeholders (-1) to represent missing teeth, based on the classes (`cls1`, `cls2`),
 * directions (`dir1`, `dir2`), and positions (`pos1`, `pos2`) of adjacent teeth.
 *
 * @param cls1 The class of the first tooth.
 * @param cls2 The class of the second tooth.
 * @param dir1 The direction ('l' for left, 'r' for right) of the first tooth.
 * @param dir2 The direction ('l' for left, 'r' for right) of the second tooth.
 * @param numMissingTeeth The initial number of detected missing teeth between cls1 and cls2.
 * @param sortedEachCls A map containing lists of tooth positions, categorized by class and direction.
 * @param pos1 The position of the first tooth.
 * @param pos2 The position of the second tooth.
 * @return A Pair containing a Boolean indicating if any missing teeth were added, and the modified map.
 */
fun findAddMissingTeeth(
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
    var numMissingTeeth = numMissingTeeth

    when (cls1) {
        0 if cls2 == 0 -> {
            numMissingTeeth = minOf(numMissingTeeth, 2)
            if (dir1 == "r" && dir2 == "l") {
                if (sortedEachCls["0"]!!["l"]!!.size < 2) {
                    sortedEachCls["0"]!!["l"]!!.add(0, -1)
                    addMissing = true
                }
                if (numMissingTeeth == 2 && sortedEachCls["0"]!!["r"]!!.size < 2) {
                    sortedEachCls["0"]!!["r"]!!.add(-1)
                    addMissing = true
                }
            }
        }
        0 if cls2 == 1 -> {
            if (dir1 == "r" && dir2 == "l") {
                numMissingTeeth = minOf(numMissingTeeth, 3)
                if (sortedEachCls["0"]!!["l"]!!.isEmpty()) {
                    sortedEachCls["0"]!!["l"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["0"]!!["l"]!!.size == 1) {
                    sortedEachCls["0"]!!["l"]!!.add(0, -1)
                    addMissing = true
                }
                if (numMissingTeeth > 2) {
                    if (sortedEachCls["0"]!!["r"]!!.size < 2) {
                        sortedEachCls["0"]!!["r"]!!.add(-1)
                        addMissing = true
                    }
                }
            } else if (dir1 == "l" && dir2 == "l") {
                numMissingTeeth = 1
                if (sortedEachCls["0"]!!["l"]!!.size < 2) {
                    sortedEachCls["0"]!!["l"]!!.add(-1)
                    addMissing = true
                }
            }
        }
        1 if cls2 == 0 -> {
            if (dir1 == "r" && dir2 == "l") {
                numMissingTeeth = minOf(numMissingTeeth, 3)
                if (sortedEachCls["0"]!!["r"]!!.isEmpty()) {
                    sortedEachCls["0"]!!["r"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["0"]!!["r"]!!.size == 1) {
                    sortedEachCls["0"]!!["r"]!!.add(0, -1)
                    addMissing = true
                }
                if (numMissingTeeth > 2) {
                    if (sortedEachCls["0"]!!["l"]!!.size < 2) {
                        sortedEachCls["0"]!!["l"]!!.add(-1)
                        addMissing = true
                    }
                }
            } else if (dir1 == "r" && dir2 == "r") {
                numMissingTeeth = 1
                if (sortedEachCls["0"]!!["r"]!!.size < 2) {
                    sortedEachCls["0"]!!["r"]!!.add(-1)
                    addMissing = true
                }
            }
        }
        0 if cls2 == 2 -> {
            if (sortedEachCls["1"]!!["l"]!!.isEmpty()) {
                sortedEachCls["1"]!!["l"] = mutableListOf(-1)
                addMissing = true
            }
            if (dir1 == "r" && dir2 == "l") {
                numMissingTeeth = maxOf(numMissingTeeth, 3)
                if (sortedEachCls["0"]!!["l"]!!.isEmpty()) {
                    sortedEachCls["0"]!!["l"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["0"]!!["l"]!!.size == 1) {
                    sortedEachCls["0"]!!["l"]!!.add(-1)
                    addMissing = true
                }
                if (numMissingTeeth == 4) {
                    if (sortedEachCls["0"]!!["r"]!!.size < 2) {
                        sortedEachCls["0"]!!["r"]!!.add(-1)
                        addMissing = true
                    }
                }
            } else if (dir1 == "l" && dir2 == "l") {
                numMissingTeeth = minOf(numMissingTeeth, 2)
                if (numMissingTeeth == 2) {
                    if (sortedEachCls["0"]!!["l"]!!.size < 2) {
                        sortedEachCls["0"]!!["l"]!!.add(-1)
                        addMissing = true
                    }
                }
            }
        }
        2 if cls2 == 0 -> {
            if (sortedEachCls["1"]!!["r"]!!.isEmpty()) {
                sortedEachCls["1"]!!["r"] = mutableListOf(-1)
                addMissing = true
            }
            if (dir1 == "r" && dir2 == "r") {
                if (numMissingTeeth == 2) {
                    if (sortedEachCls["0"]!!["r"]!!.size < 2) {
                        sortedEachCls["0"]!!["r"]!!.add(0, -1)
                        addMissing = true
                    }
                }
            } else if (dir1 == "r" && dir2 == "l") {
                numMissingTeeth = maxOf(numMissingTeeth, 3)
                if (sortedEachCls["0"]!!["r"]!!.isEmpty()) {
                    sortedEachCls["0"]!!["r"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["0"]!!["r"]!!.size == 1) {
                    sortedEachCls["0"]!!["r"]!!.add(-1)
                    addMissing = true
                }
                if (numMissingTeeth == 4) {
                    if (sortedEachCls["0"]!!["l"]!!.size < 2) {
                        sortedEachCls["0"]!!["l"]!!.add(-1)
                        addMissing = true
                    }
                }
            }
        }
        0 if cls2 == 3 -> {
            if (dir1 == "l" && dir2 == "l") {
                numMissingTeeth = maxOf(numMissingTeeth, 3)
                if (sortedEachCls["2"]!!["l"]!!.isEmpty()) {
                    sortedEachCls["2"]!!["l"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["2"]!!["l"]!!.size == 1) {
                    sortedEachCls["2"]!!["l"]!!.add(-1)
                    addMissing = true
                }
                if (sortedEachCls["1"]!!["l"]!!.isEmpty()) {
                    sortedEachCls["1"]!!["l"] = mutableListOf(-1)
                    addMissing = true
                }
                if (numMissingTeeth == 4) {
                    if (sortedEachCls["3"]!!["l"]!!.size < 3) {
                        sortedEachCls["3"]!!["l"]!!.add(0, -1)
                        addMissing = true
                    }
                }
            }
        }
        3 if cls2 == 0 -> {
            if (dir1 == "r" && dir2 == "r") {
                numMissingTeeth = maxOf(numMissingTeeth, 3)
                if (sortedEachCls["2"]!!["r"]!!.isEmpty()) {
                    sortedEachCls["2"]!!["r"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["2"]!!["r"]!!.size == 1) {
                    sortedEachCls["2"]!!["r"]!!.add(-1)
                    addMissing = true
                }
                if (sortedEachCls["1"]!!["r"]!!.isEmpty()) {
                    sortedEachCls["1"]!!["r"] = mutableListOf(-1)
                    addMissing = true
                }
                if (numMissingTeeth == 4) {
                    if (sortedEachCls["3"]!!["r"]!!.size < 3) {
                        sortedEachCls["3"]!!["r"]!!.add(-1)
                        addMissing = true
                    }
                }
            }
        }
        1 if cls2 == 1 -> {
            numMissingTeeth = 4
            if (sortedEachCls["0"]!!["r"]!!.isEmpty()) {
                sortedEachCls["0"]!!["r"] = mutableListOf(-1, -1)
                addMissing = true
            } else if (sortedEachCls["0"]!!["r"]!!.size == 1) {
                sortedEachCls["0"]!!["r"]!!.add(-1)
                addMissing = true
            }
            if (sortedEachCls["0"]!!["l"]!!.isEmpty()) {
                sortedEachCls["0"]!!["l"] = mutableListOf(-1, -1)
                addMissing = true
            } else if (sortedEachCls["0"]!!["l"]!!.size == 1) {
                sortedEachCls["0"]!!["l"]!!.add(-1)
                addMissing = true
            }
        }
        1 if cls2 == 2 -> {
            if (dir1 == "l" && dir2 == "l") {
                numMissingTeeth = 1
                if (sortedEachCls["2"]!!["l"]!!.size < 2) {
                    sortedEachCls["2"]!!["l"]!!.add(0, -1)
                    addMissing = true
                }
            }
        }
        2 if cls2 == 1 -> {
            if (dir1 == "r" && dir2 == "r") {
                numMissingTeeth = 1
                if (sortedEachCls["2"]!!["r"]!!.size < 2) {
                    sortedEachCls["2"]!!["r"]!!.add(-1)
                    addMissing = true
                }
            }
        }
        1 if cls2 == 3 -> {
            if (dir1 == "l" && dir2 == "l") {
                numMissingTeeth = maxOf(numMissingTeeth, 2)
                if (sortedEachCls["2"]!!["l"]!!.isEmpty()) {
                    sortedEachCls["2"]!!["l"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["2"]!!["l"]!!.size == 1) {
                    sortedEachCls["2"]!!["l"]!!.add(-1)
                    addMissing = true
                }
                if (numMissingTeeth == 3) {
                    if (sortedEachCls["3"]!!["l"]!!.size < 3) {
                        sortedEachCls["3"]!!["l"]!!.add(0, -1)
                        addMissing = true
                    }
                } else if (numMissingTeeth == 4) {
                    if (sortedEachCls["3"]!!["l"]!!.size == 2) {
                        sortedEachCls["3"]!!["l"]!!.add(0, -1)
                        addMissing = true
                    } else if (sortedEachCls["3"]!!["l"]!!.size < 2) {
                        sortedEachCls["3"]!!["l"]!!.addAll(0, mutableListOf(-1, -1))
                        addMissing = true
                    }
                }
            }
        }
        3 if cls2 == 1 -> {
            if (dir1 == "r" && dir2 == "r") {
                numMissingTeeth = maxOf(numMissingTeeth, 2)
                if (sortedEachCls["2"]!!["r"]!!.isEmpty()) {
                    sortedEachCls["2"]!!["r"] = mutableListOf(-1, -1)
                    addMissing = true
                } else if (sortedEachCls["2"]!!["r"]!!.size == 1) {
                    sortedEachCls["2"]!!["r"]!!.add(-1)
                    addMissing = true
                }
                if (numMissingTeeth == 3) {
                    if (sortedEachCls["3"]!!["r"]!!.size < 3) {
                        sortedEachCls["3"]!!["r"]!!.add(-1)
                        addMissing = true
                    }
                } else if (numMissingTeeth == 4) {
                    if (sortedEachCls["3"]!!["r"]!!.size == 2) {
                        sortedEachCls["3"]!!["r"]!!.add(-1)
                        addMissing = true
                    } else if (sortedEachCls["3"]!!["r"]!!.size < 2) {
                        sortedEachCls["3"]!!["r"]!!.addAll(mutableListOf(-1, -1))
                        addMissing = true
                    }
                }
            }
        }
        2 if cls2 == 3 -> {
            if (dir1 == "l" && dir2 == "l") {
                numMissingTeeth = minOf(numMissingTeeth, 3)
                if (numMissingTeeth == 3) {
                    if (sortedEachCls["2"]!!["l"]!!.size < 2) {
                        sortedEachCls["2"]!!["l"]!!.add(-1)
                        addMissing = true
                    }
                    if (sortedEachCls["3"]!!["l"]!!.size < 2) {
                        sortedEachCls["3"]!!["l"]!!.add(0, -1)
                        addMissing = true
                    }
                } else {
                    if (sortedEachCls["2"]!!["l"]!!.size == 2) {
                        if (sortedEachCls["3"]!!["l"]!!.size < 2 && numMissingTeeth == 2) {
                            sortedEachCls["3"]!!["l"]!!.addAll(0, mutableListOf(-1, -1))
                            addMissing = true
                        } else if (sortedEachCls["3"]!!["l"]!!.size < 3) {
                            sortedEachCls["3"]!!["l"]!!.add(0, -1)
                            addMissing = true
                        }
                    } else {
                        if (sortedEachCls["2"]!!["l"]!!.size < 2) {
                            sortedEachCls["2"]!!["l"]!!.add(-1)
                            addMissing = true
                        }
                        if (numMissingTeeth == 2) {
                            if (sortedEachCls["3"]!!["l"]!!.size < 3) {
                                sortedEachCls["3"]!!["l"]!!.add(0, -1)
                                addMissing = true
                            }
                        }
                    }
                }
            }
        }
        3 if cls2 == 2 -> {
            if (dir1 == "r" && dir2 == "r") {
                numMissingTeeth = minOf(numMissingTeeth, 3)
                if (numMissingTeeth == 3) {
                    if (sortedEachCls["2"]!!["r"]!!.size < 2) {
                        sortedEachCls["2"]!!["r"]!!.add(0, -1)
                        addMissing = true
                    }
                    if (sortedEachCls["3"]!!["r"]!!.size < 2) {
                        sortedEachCls["3"]!!["r"]!!.addAll(mutableListOf(-1, -1))
                        addMissing = true
                    }
                } else {
                    if (sortedEachCls["2"]!!["r"]!!.size == 2) {
                        if (sortedEachCls["3"]!!["r"]!!.size < 2 && numMissingTeeth == 2) {
                            sortedEachCls["3"]!!["r"]!!.addAll(mutableListOf(-1, -1))
                            addMissing = true
                        } else if (sortedEachCls["3"]!!["r"]!!.size < 3) {
                            sortedEachCls["3"]!!["r"]!!.add(-1)
                            addMissing = true
                        }
                    } else {
                        if (sortedEachCls["2"]!!["r"]!!.size < 2) {
                            sortedEachCls["2"]!!["r"]!!.add(0, -1)
                            addMissing = true
                        }
                        if (numMissingTeeth == 2) {
                            if (sortedEachCls["3"]!!["r"]!!.size < 3) {
                                sortedEachCls["3"]!!["r"]!!.add(-1)
                                addMissing = true
                            }
                        }
                    }
                }
            }
        }
        3 if cls2 == 3 -> {
            numMissingTeeth = 1
            if (dir1 == "r" && dir2 == "r") {
                if (sortedEachCls["3"]!!["r"]!!.size == 2) {
                    sortedEachCls.getOrPut("3") { mutableMapOf() }["r"] =
                        mutableListOf(pos1, -1, pos2)
                    addMissing = true
                }
            } else if (dir1 == "l" && dir2 == "l") {
                if (sortedEachCls["3"]!!["l"]!!.size == 2) {
                    sortedEachCls.getOrPut("3") { mutableMapOf() }["l"] =
                        mutableListOf(pos1, -1, pos2)
                    addMissing = true
                }
            }
        }
    }
    return Pair(addMissing, sortedEachCls)
}
