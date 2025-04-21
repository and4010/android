package com.example.survival.ui.second

import androidx.lifecycle.ViewModel
import com.example.survival.ui.second.SecondRepo.PatternDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SecondViewModel
@Inject constructor(
    private val secondRepo: SecondRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecondState())
    val uiState = _uiState.asStateFlow()


    init {
        _uiState.update { u -> u.copy(patternDto = secondRepo.getColorList(), currentColor = secondRepo.getDefaultColor()) }
    }




    fun changeColor(pattern: Pattern) {
        _uiState.update {
            u -> u.copy(currentColor = pattern.color)
        }
    }

}


data class SecondState(
    val currentColor : Long = 0L,
    val patternDto: Map<String, List<Pattern>> = mapOf()
)


data class Pattern(
    val hue: String = "",
    val level: Int = 0,
    val color: Long = 0L
)



fun PatternDto.toPattern(): Pattern {
    return Pattern(hue, level, color)
}