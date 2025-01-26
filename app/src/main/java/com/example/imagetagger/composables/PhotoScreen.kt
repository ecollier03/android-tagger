package com.example.imagetagger.composables

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.support.annotation.RequiresPermission
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.imagetagger.R
import com.example.imagetagger.models.FileEntry
import com.example.imagetagger.models.ViewModel

@RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
@Composable
fun MediaStoreQueryContent() {
//    val context = LocalContext.current
    val viewModel: ViewModel = hiltViewModel()

    val loadedFiles = remember { mutableStateListOf<FileEntry>() }
    val files by viewModel.photoQuery.collectAsState()
    val lazyGridState = rememberLazyStaggeredGridState()

    PhotoGrid(modifier = Modifier)

}


@Composable
fun CenteredLoadingSpinner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp), // Spinner size
            color = MaterialTheme.colorScheme.primary, // Spinner color
            strokeWidth = 4.dp // Thickness of the spinner
        )
    }
}

@Composable
fun PhotoGrid(
    modifier: Modifier
) {
    val viewModel: ViewModel = hiltViewModel()
    val lazyPagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    Log.i("Grid", "Loading Grid")

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(
            lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.uri }
        )
        { index ->
            val file = lazyPagingItems[index]
            if (file != null) {
                AsyncImage(
                    model = file.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.placeholder_gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            Log.d("Hi", "HELLO!")
                        }
                )
            } else {
                Loading(modifier)
            }
        }
    }
}

@Composable
fun Loading(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        CircularProgressIndicator()
    }
}
