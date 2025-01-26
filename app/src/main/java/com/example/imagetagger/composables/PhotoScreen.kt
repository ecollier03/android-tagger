package com.example.imagetagger.composables

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.ContentUris
import android.provider.MediaStore.Images
import android.support.annotation.RequiresPermission
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import android.content.ContentResolver
import android.os.Build
import android.provider.MediaStore
import android.text.format.Formatter
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.net.Uri

@RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
@Composable
fun MediaStoreQueryContent() {
    val context = LocalContext.current
    val files by loadImages(context.contentResolver)

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            ListItem(
                headlineContent = {
                    if (files.isNotEmpty()) {
                        Text("${files.size} images found")
                    }
                },
            )
            HorizontalDivider()
        }

        items(files) { file ->
            ListItem(
                leadingContent = {
                    AsyncImage(
                        model = file.uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .aspectRatio(1f),
                    )
                },
                headlineContent = {
                    Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                supportingContent = { Text(file.mimeType) },
                trailingContent = { Text(Formatter.formatShortFileSize(context, file.size)) },
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun loadImages(
    contentResolver: ContentResolver,
): State<List<FileEntry>> = produceState(initialValue = emptyList()) {
    value = getImages(contentResolver)
}

data class FileEntry(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
    val dateAdded: Long,
)

