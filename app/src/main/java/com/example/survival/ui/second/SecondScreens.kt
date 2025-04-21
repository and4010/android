package com.example.survival.ui.second

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun SecondScreen(viewModel: SecondViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()



    Column(modifier = Modifier.fillMaxSize()) {
        SecondContent(
            state = state,
            onClick = { pattern ->
                viewModel.changeColor(pattern)
            }
        )
    }

}


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun SecondContent(state: SecondState, onClick: (Pattern) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(state.currentColor))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                state.patternDto.forEach { hue, patternList ->
                    stickyHeader {
                        Text(
                            color = Color.Black,
                            text = hue,
                            fontSize = 15.sp,
                            modifier = Modifier.background(Color.White).fillMaxWidth()
                        )
                    }

                    item {
                        FlowRow(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            patternList.forEach { pattern ->
                                Footer(pattern) {
                                    onClick(pattern)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Footer(item: Pattern, onClick: (Pattern) -> Unit) {

    Box(
        modifier = Modifier.padding(16.dp)
            .size(50.dp)
            .clip(CircleShape)
            .background(Color(item.color)).clickable{
                onClick(item)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.level.toString(),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }

}


@Preview
@Composable
private fun SecondScreenPreview() {


    val json = "{\"Orange\":[{\"color\":4294964192,\"hue\":\"Orange\",\"level\":50},{\"color\":4294954112,\"hue\":\"Orange\",\"level\":200},{\"color\":4294948685,\"hue\":\"Orange\",\"level\":300},{\"color\":4294944550,\"hue\":\"Orange\",\"level\":400},{\"color\":4294940672,\"hue\":\"Orange\",\"level\":500},{\"color\":4294675456,\"hue\":\"Orange\",\"level\":600},{\"color\":4294278144,\"hue\":\"Orange\",\"level\":700},{\"color\":4293880832,\"hue\":\"Orange\",\"level\":800}],\"Cyan\":[{\"color\":4289915890,\"hue\":\"Cyan\",\"level\":100},{\"color\":4286635754,\"hue\":\"Cyan\",\"level\":200},{\"color\":4278234305,\"hue\":\"Cyan\",\"level\":600},{\"color\":4278228903,\"hue\":\"Cyan\",\"level\":700},{\"color\":4278214756,\"hue\":\"Cyan\",\"level\":900}],\"Brown\":[{\"color\":4292332744,\"hue\":\"Brown\",\"level\":100},{\"color\":4290554532,\"hue\":\"Brown\",\"level\":200},{\"color\":4288776319,\"hue\":\"Brown\",\"level\":300},{\"color\":4287458915,\"hue\":\"Brown\",\"level\":400},{\"color\":4286141768,\"hue\":\"Brown\",\"level\":500},{\"color\":4284301367,\"hue\":\"Brown\",\"level\":700},{\"color\":4283315246,\"hue\":\"Brown\",\"level\":800}],\"Red\":[{\"color\":4293892762,\"hue\":\"Red\",\"level\":200},{\"color\":4294198070,\"hue\":\"Red\",\"level\":500},{\"color\":4291176488,\"hue\":\"Red\",\"level\":800}],\"Indigo\":[{\"color\":4288653530,\"hue\":\"Indigo\",\"level\":200},{\"color\":4286154443,\"hue\":\"Indigo\",\"level\":300},{\"color\":4281352095,\"hue\":\"Indigo\",\"level\":700}],\"Purple\":[{\"color\":4290406600,\"hue\":\"Purple\",\"level\":300}],\"Teal\":[{\"color\":4280723098,\"hue\":\"Teal\",\"level\":400}]}"

    val type = object : TypeToken<Map<String, List<Pattern>>>() {}.type
    val patternMap: Map<String, List<Pattern>> = Gson().fromJson(json, type)

    SecondContent(state = SecondState(patternDto = patternMap))
}