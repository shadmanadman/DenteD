package preprocessing

val lowerNumberingMapping: Map<String, Map<String, List<String>>> = mapOf(
    "0" to mapOf("r" to listOf("lr2", "lr1"), "l" to listOf("ll1", "ll2")),
    "1" to mapOf("r" to listOf("lr3"), "l" to listOf("ll3")),
    "2" to mapOf("r" to listOf("lr5", "lr4"), "l" to listOf("ll4", "ll5")),
    "3" to mapOf("r" to listOf("lr8", "lr7", "lr6"), "l" to listOf("ll6", "ll7", "ll8"))
)
val upperNumberingMapping: Map<String, Map<String, List<String>>> = mapOf(
    "0" to mapOf("r" to listOf("ur2", "ur1"), "l" to listOf("ul1", "ul2")),
    "1" to mapOf("r" to listOf("ur3"), "l" to listOf("ul3")),
    "2" to mapOf("r" to listOf("ur5", "ur4"), "l" to listOf("ul4", "ul5")),
    "3" to mapOf("r" to listOf("ur8", "ur7", "ur6"), "l" to listOf("ul6", "ul7", "ul8"))
)
val lowerNumberingInds: Map<String, Int> = mapOf(
    "ll1" to 0, "ll2" to 1, "ll3" to 2, "ll4" to 3,
    "ll5" to 4, "ll6" to 5, "ll7" to 6, "ll8" to 7,
    "lr1" to 8, "lr2" to 9, "lr3" to 10, "lr4" to 11,
    "lr5" to 12, "lr6" to 13, "lr7" to 14, "lr8" to 15
)
val upperNumberingInds: Map<String, Int> = mapOf(
    "ul1" to 0, "ul2" to 1, "ul3" to 2, "ul4" to 3,
    "ul5" to 4, "ul6" to 5, "ul7" to 6, "ul8" to 7,
    "ur1" to 8, "ur2" to 9, "ur3" to 10, "ur4" to 11,
    "ur5" to 12, "ur6" to 13, "ur7" to 14, "ur8" to 15)


val frontLowerNumberingMapping: Map<String, Map<String, List<String>>> = mapOf(
"4" to mapOf("r" to listOf("lr2", "lr1"), "l" to listOf("ll1", "ll2")),
"5" to mapOf("r" to listOf("lr3"), "l" to listOf("ll3")),
"6" to mapOf("r" to listOf("lr5", "lr4"), "l" to listOf("ll4", "ll5")),
"7" to mapOf("r" to listOf("lr8", "lr7", "lr6"), "l" to listOf("ll6", "ll7", "ll8"))
)

