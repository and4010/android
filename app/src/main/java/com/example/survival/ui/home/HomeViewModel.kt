package com.example.survival.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepo
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()


    fun removeData(index: Int) {
        homeRepo.removeData(index)
        _uiState.update {
                u -> u.copy(itemList = getListData())
        }
    }

    fun insertData(value : String){
        homeRepo.addData(value)
        _uiState.update {
                u -> u.copy(itemList = getListData())
        }
    }

    fun updateEditingIndex(editingIndex:Int){
        _uiState.update {
                u -> u.copy(editingIndex = editingIndex)
        }
    }

    fun editData(value: String, index: Int){
        homeRepo.editData(value, index)
        _uiState.update {
                u -> u.copy(itemList = getListData(), editingIndex = -1)
        }
    }

    fun clearData(){
        homeRepo.clearData()
        _uiState.update {
                u -> u.copy(itemList = getListData(), editingIndex = -1)
        }
    }

    fun getListData() : List<String>{
      return homeRepo.getData()
    }






}


/**
 * @param text 顯示字
 */
data class HomeState(
    val text : String = "",
    val itemList : List<String> = emptyList(),
    val editingIndex : Int = -1
)