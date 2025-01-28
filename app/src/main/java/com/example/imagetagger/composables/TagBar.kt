package com.example.imagetagger.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.imagetagger.models.ViewModel

@Composable
fun TagBar(
    onClick: (String) -> Unit,
    text: String,
) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel: ViewModel = hiltViewModel()
    val tags by viewModel.allTags.collectAsState()
    LaunchedEffect(tags) {
        Log.i("Tags", "tags have been updated?")
    }

    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
                    .align(Alignment.Center)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            tags.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onClick(option) }
                )
            }
        }
    }

}