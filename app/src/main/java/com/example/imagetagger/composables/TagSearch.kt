package com.example.imagetagger.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagetagger.models.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun TagHeader() {
    val viewModel: ViewModel = hiltViewModel()
    var newTagText by remember { mutableStateOf("Hello") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LongBasicDropdownMenu(
            onClick = { Log.i("Clicked", it) }
        ) {
            Text(
                "Tags",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        TextField(
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

}

@Composable
fun LongBasicDropdownMenu(
    onClick: (String) -> Unit,
    content: @Composable (BoxScope.() -> Unit),
) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel: ViewModel = hiltViewModel()
    val tags by viewModel.allTags.collectAsState()
    LaunchedEffect(tags) {
        Log.i("Tags", "tags have been updated?")
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                }
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(10.dp)
        ) {
            content()
        }
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