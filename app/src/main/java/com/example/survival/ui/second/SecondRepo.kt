package com.example.survival.ui.second

class SecondRepo {



    val patternsDtoList = listOf(
        PatternDto("Red", 500, 0xFFF44336),
        PatternDto("Cyan", 200, 0xFF80DEEA),
        PatternDto("Orange", 50, 0xFFFFF3E0),
        PatternDto("Cyan", 700, 0xFF0097A7),
        PatternDto("Cyan", 900, 0xFF006064),
        PatternDto("Brown", 700, 0xFF5D4037),
        PatternDto("Orange", 600, 0xFFFB8C00),
        PatternDto("Orange", 700, 0xFFF57C00),
        PatternDto("Brown", 800, 0xFF4E342E),
        PatternDto("Orange", 400, 0xFFFFA726),
        PatternDto("Brown", 500, 0xFF795548),
        PatternDto("Orange", 800, 0xFFEF6C00),
        PatternDto("Brown", 300, 0xFFA1887F),
        PatternDto("Red", 200, 0xFFEF9A9A),
        PatternDto("Teal", 400, 0xFF26A69A),
        PatternDto("Cyan", 100, 0xFFB2EBF2),
        PatternDto("Brown", 400, 0xFF8D6E63),
        PatternDto("Orange", 300, 0xFFFFB74D),
        PatternDto("Indigo", 700, 0xFF303F9F),
        PatternDto("Orange", 200, 0xFFFFCC80),
        PatternDto("Purple", 300, 0xFFBA68C8),
        PatternDto("Brown", 100, 0xFFD7CCC8),
        PatternDto("Cyan", 600, 0xFF00ACC1),
        PatternDto("Orange", 500, 0xFFFF9800),
        PatternDto("Indigo", 300, 0xFF7986CB),
        PatternDto("Red", 800, 0xFFC62828),
        PatternDto("Indigo", 200, 0xFF9FA8DA),
        PatternDto("Brown", 200, 0xFFBCAAA4)
    )


    fun getColorList() : Map<String, List<Pattern>> {
        val patterns = patternsDtoList.map { it.toPattern() }
        return patterns.sortedBy { it.level } .groupBy { it.hue }
    }



    fun getDefaultColor(): Long {
        return getColorList().entries.firstOrNull()?.value?.firstOrNull()?.color ?: 0L
    }







    data class PatternDto(
        val hue: String,
        val level: Int,
        val color: Long
    )

}