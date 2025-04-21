package com.example.survival.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        Content(
            state, onInsertOrEdit = { text, index ->
                if (index == -1) {
                    viewModel.insertData(text)
                } else {
                    viewModel.editData(text, index)
                }

            },
            onEditClick = {
                viewModel.updateEditingIndex(it)
            },

            onRemoveClick = {
                viewModel.removeData(it)
            },
            onClearClick = {
                viewModel.clearData()
            })
    }
}

@Composable
private fun Content(
    state: HomeState,
    onInsertOrEdit: (text: String, index: Int) -> Unit,
    onEditClick: (Int) -> Unit = {},
    onRemoveClick: (Int) -> Unit = {},
    onClearClick: () -> Unit = {}
) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = {
                Text(if (state.editingIndex == -1) "輸入文字" else "編輯文字")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                if (inputText.text.isEmpty()) {
                    return@Button
                }
                onInsertOrEdit(inputText.text, state.editingIndex)
                inputText = TextFieldValue("")
            }) {
                Text(if (state.editingIndex == -1) "新增" else "更新")
            }

            Button(onClick = {
                onClearClick()
                inputText = TextFieldValue("")
            }) {
                Text("清除")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("已新增項目：")
        LazyColumn {
            itemsIndexed(state.itemList) { index ,item->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("- $item", modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        onEditClick(index)
                        inputText = TextFieldValue(
                            text = item,
                            selection = TextRange(item.length)
                        )
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "編輯")
                    }
                    IconButton(onClick = {
                        onRemoveClick(index)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "刪除")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Content(state = HomeState(), onInsertOrEdit = { _, _ -> })
}