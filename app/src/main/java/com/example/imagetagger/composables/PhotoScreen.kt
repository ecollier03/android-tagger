package com.example.imagetagger.composables

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.support.annotation.RequiresPermission
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel
import kotlinx.coroutines.delay

@RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
@Composable
fun MainPhotoScreen(
    onPhotoClick: (Int) -> Unit,
    index: Int,
) {
    val viewModel: ViewModel = hiltViewModel()
    val photos = viewModel.photoQuery.collectAsState()
    val state = rememberLazyStaggeredGridState(index)
    val zoom by remember { mutableIntStateOf(200) }

    Log.i("Photos", "There are ${photos.value.size} photos")

    LaunchedEffect(photos) {
        viewModel.fetchAllPhotos()
    }

    LaunchedEffect(state) {
        delay(10)
        state.animateScrollToItem(index)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        TagHeader()
        Box(modifier = Modifier.padding(20.dp)) {
            LazyVerticalStaggeredGrid(
                state = state,
                columns = StaggeredGridCells.Adaptive(zoom.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(photos.value)  { index, item ->
                    AsyncImage(
                        model = item.uri,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.placeholder_gray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable {
                                Log.d("ViewList", "Clicked photo $index")
                                onPhotoClick(index)
                            }
                    )
                }
            }

        }
    }
}