package com.example.imagetagger.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.imagetagger.models.ViewModel

@Composable
fun TagHeader() {
    val viewModel: ViewModel = hiltViewModel()
    var newTagText by remember { mutableStateOf("") }
    var searchQueryText by rememberSaveable { mutableStateOf("") }


    //There are 2 rows of the header
    Column(modifier = Modifier) {
        TagBar(
            onClick = {
                Log.d("TagBar", "Clicked")
            },
            text = "Tags"
        )
        Row( // the Tags dropdown and tag submission button
            modifier = Modifier
                .fillMaxWidth()
        ) { TextField(
                value = newTagText,
                onValueChange = { newTagText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                trailingIcon =  {
                    Button(
                        onClick = {
                            viewModel.submitTag(newTagText)
                            newTagText = ""
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                    ) {
                        Text("X")
                    }
                }
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQueryText,
                onValueChange = { searchQueryText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                trailingIcon =  {
                    Button(
                        onClick = {
                            viewModel.submitTag(newTagText)
                            newTagText = ""
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                    ) {
                        Text("X")
                    }
                }
            )
        }
    }
}

