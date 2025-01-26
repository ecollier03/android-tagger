package com.example.imagetagger

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.imagetagger.models.ViewModel
import com.example.imagetagger.composables.*
import com.example.imagetagger.ui.theme.ImageTaggerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Source https://developer.android.com/develop/ui/compose/designsystems/material3#dynamic_color_schemes
            // Check if the current android version supports Material You
            val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            val darkTheme = isSystemInDarkTheme()
            val colors = when {
                dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
                dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
                darkTheme -> darkColorScheme()
                else -> lightColorScheme()
            }
            MaterialTheme(
                colorScheme = colors
            ) {
                Box(Modifier.safeDrawingPadding()) {
                    MainEntry()
                }
            }
        }
    }
}


@Serializable
object StartScreen

@Serializable
object PhotoScreen

@SuppressLint("MissingPermission")
@Composable
fun MainEntry(viewModel: ViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val backHandler: (destination: Any) -> Unit = { destination ->
        when (destination) {
            PhotoScreen -> {
                navController.navigate(PhotoScreen) {
                    popUpTo(PhotoScreen) { inclusive = false }
                }
            }
        }
    }

    NavHost(navController, startDestination = StartScreen) {
        composable<StartScreen> {
            StartScreen(
                modifier = Modifier,
                onEnterPressed = {
                    backHandler(PhotoScreen)
                }
            )
        }
        composable<PhotoScreen> {
            MediaStoreQueryContent()
        }
    }
}


//val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//    READ_MEDIA_IMAGES
//} else {
//    READ_EXTERNAL_STORAGE
//}

//@RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
//@Composable
//fun MediaStoreQueryContent() {
//    Log.i("hi", "media querey time")
//    val context = LocalContext.current
//    val files by loadImages(context.contentResolver)
//
//    LazyColumn(Modifier.fillMaxSize()) {
//        item {
//            ListItem(
//                headlineContent = {
//                    if (files.isNotEmpty()) {
//                        Text("${files.size} images found")
//                    }
//                },
//            )
//            HorizontalDivider()
//        }
//
//        items(files) { file ->
//            ListItem(
//                leadingContent = {
//                    AsyncImage(
//                        model = file.uri,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(64.dp)
//                            .aspectRatio(1f),
//                    )
//                },
//                headlineContent = {
//                    Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                },
//                supportingContent = { Text(file.mimeType) },
//                trailingContent = { Text(Formatter.formatShortFileSize(context, file.size)) },
//            )
//            HorizontalDivider()
//        }
//    }
//}
//
//@Composable
//private fun loadImages(
//    contentResolver: ContentResolver,
//): State<List<FileEntry>> = produceState(initialValue = emptyList()) {
//    value = getImages(contentResolver)
//}
//data class FileEntry(
//    val uri: Uri,
//    val name: String,
//    val size: Long,
//    val mimeType: String,
//    val dateAdded: Long,
//)
//
///**
// * Query [MediaStore] through [ContentResolver] to get all images sorted by added date by targeting
// * the [Images] collection
// */
//private suspend fun getImages(contentResolver: ContentResolver): List<FileEntry> {
//    return withContext(Dispatchers.IO) {
//        // List of columns we want to fetch
//        val projection = arrayOf(
//            Images.Media._ID,
//            Images.Media.DISPLAY_NAME,
//            Images.Media.SIZE,
//            Images.Media.MIME_TYPE,
//            Images.Media.DATE_ADDED,
//        )
//
//        val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // This allows us to query all the device storage volumes instead of the primary only
//            Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
//        } else {
//            Images.Media.EXTERNAL_CONTENT_URI
//        }
//
//        val images = mutableListOf<FileEntry>()
//
//        contentResolver.query(
//            collectionUri, // Queried collection
//            projection, // List of columns we want to fetch
//            null, // Filtering parameters (in this case none)
//            null, // Filtering values (in this case none)
//            "${Images.Media.DATE_ADDED} DESC", // Sorting order (recent -> older files)
//        )?.use { cursor ->
//            val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
//            val displayNameColumn = cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME)
//            val sizeColumn = cursor.getColumnIndexOrThrow(Images.Media.SIZE)
//            val mimeTypeColumn = cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE)
//            val dateAddedColumn = cursor.getColumnIndexOrThrow(Images.Media.DATE_ADDED)
//
//            while (cursor.moveToNext()) {
//                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
//                val name = cursor.getString(displayNameColumn)
//                val size = cursor.getLong(sizeColumn)
//                val mimeType = cursor.getString(mimeTypeColumn)
//                val dateAdded = cursor.getLong(dateAddedColumn)
//
//                images.add(FileEntry(uri, name, size, mimeType, dateAdded))
//            }
//        }
//
//        return@withContext images
//    }
//}