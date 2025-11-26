package preprocessing


/**
 * Visible boxes that can been seen in the screen with a small padding. the hole box should
 * be visible to count as visible.
 */
fun List<ToothBox>.filterVisibleBoxes(normalizedPadding: Float): List<ToothBox> {
    return this.filter { box ->
        println("box: $box")

        val originX = box.x - (box.width / 2)
        println("originX: $originX")

        val originY = box.y - (box.height / 2)
        println("originY: $originY")

        val endX = originX + box.width
        println("endX: $endX")

        val endY = originY + box.height
        println("endY: $endY")

        val padding = 0.005f

        originX - padding >= normalizedPadding && endX + padding <= 1 - normalizedPadding &&
                originY - padding >= 0 && endY + padding <= 1
    }
}

fun MutableList<ToothBox>.exportTeethCoordinate(): Array<DoubleArray> {
    val teethCoordinate = Array(this.size) { DoubleArray(5) }

    for ((index, box) in this.withIndex()) {
        teethCoordinate[index][0] =
            box.category.toDouble() // Assuming number represents the category ID
        teethCoordinate[index][1] = box.x.toDouble()
        teethCoordinate[index][2] = box.y.toDouble()
        teethCoordinate[index][3] = box.width.toDouble()
        teethCoordinate[index][4] = box.height.toDouble()
    }

    return teethCoordinate
}

