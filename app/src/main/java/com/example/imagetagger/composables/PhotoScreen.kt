package com.example.imagetagger.composables

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.net.Uri
import android.support.annotation.RequiresPermission
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel

@RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
@Composable
fun MainPhotoScreen(
    onPhotoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .safeDrawingPadding()
    ) {
        TagHeader()
        Box(modifier = Modifier.padding(20.dp)) {
            PhotoGrid(
                modifier = Modifier,
                onPhotoClick = onPhotoClick
            )
        }

    }

}


@Composable
fun PhotoGrid(
    modifier: Modifier,
    onPhotoClick: (Int) -> Unit
) {
    val viewModel: ViewModel = hiltViewModel()
    val lazyPagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    Log.i("Grid", "Loading Grid with ${lazyPagingItems.itemCount}")

    LazyColumn(
//        columns = GridCells.Adaptive(200.dp),
//        verticalItemSpacing = 4.dp,
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
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
                            onPhotoClick(index)
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
