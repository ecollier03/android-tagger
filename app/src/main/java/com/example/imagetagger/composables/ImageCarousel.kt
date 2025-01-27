package com.example.imagetagger.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel
import kotlinx.coroutines.launch


@Composable
fun ImageCarousel(
    initialIndex: Int,
    modifier: Modifier
){

    val viewModel: ViewModel = hiltViewModel()
    var currentIndex by remember { mutableIntStateOf(initialIndex) }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState(initialIndex)
    val photos = viewModel.photoQuery.collectAsState()
    LaunchedEffect(photos) {
        viewModel.fetchAllPhotos()
    }

    LazyRow(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
    ) {
        itemsIndexed(photos.value) { index, photo ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(20.dp).fillParentMaxSize().safeDrawingPadding(),
            ) {
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.placeholder_gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .safeDrawingPadding()
                        .padding(16.dp)
                )
            }
        }
    }
    // Update the currentIndex as the LazyRow scrolls
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { visibleIndex ->
                currentIndex = visibleIndex
            }
    }

    // Example of showing the current photo index
    Text(
        text = "Photo ${currentIndex + 1} / ${photos.value.size}",
        modifier = Modifier
            .padding(16.dp)
    )


}