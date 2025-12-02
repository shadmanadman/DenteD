package postprocessing

import kotlin.collections.iterator


/**
 * Find the indices of the missing value in the given sorted_each_cls dictionary and map them to the corresponding
 * lower or upper numbering indices based on the img_type.
 */
fun defineNumberingMissing(
    sortedEachCls: MutableMap<String, MutableMap<String, MutableList<Int>>>,
    imgType: String,
    subJawType: String,
    missingValue: Int = -1
): List<String> {
    val numberingMissing = mutableListOf<String>()

    for ((category, innerDict) in sortedEachCls) {
        for ((dir, values) in innerDict) {
            val ind = values.indexOf(missingValue)
            if (ind != -1) {
                if (imgType == "lower") {
                    if (subJawType == "front") {
                        val lowerMapping = frontLowerNumberingMapping[category]?.get(dir)
                        if (lowerMapping != null && lowerMapping.size > ind) {
                            numberingMissing.add(lowerMapping[ind])
                        }
                    } else {
                        val lowerMapping = lowerNumberingMapping[category]?.get(dir)
                        if (lowerMapping != null && lowerMapping.size > ind) {
                            numberingMissing.add(lowerMapping[ind])
                        }
                    }
                } else if (imgType == "upper") {
                    val upperMapping = upperNumberingMapping[category]?.get(dir)
                    if (upperMapping != null && upperMapping.size > ind) {
                        numberingMissing.add(upperMapping[ind])
                    }
                }
            }
        }
    }

    return numberingMissing
}