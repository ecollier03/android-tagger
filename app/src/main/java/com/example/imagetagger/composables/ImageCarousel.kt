package com.example.imagetagger.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ImageCarousel(
    initialIndex: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier
){
    val viewModel: ViewModel = hiltViewModel()
    val photos = viewModel.photoQuery.collectAsState()
    val pagerState = rememberPagerState(initialPage = initialIndex) {photos.value.size}

    LaunchedEffect(photos) {
        viewModel.fetchAllPhotos()
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect {
                onPageChange(it)
            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { page ->
        AsyncImage(
            model = photos.value[page].uri,
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

//LazyRow(
//state = lazyListState,
//modifier = Modifier
//.fillMaxSize()
//.background(MaterialTheme.colorScheme.background),
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures { _, dragAmount ->
//                    if (dragAmount > 0) { // swipe right
//                        scope.launch{
//                            val currentIndex = lazyListState.firstVisibleItemIndex
//                            lazyListState.animateScrollToItem(currentIndex - 1)
//                        }
//                    } else if (dragAmount < 0) { // swipe left
//                        scope.launch{
//                            val currentIndex = lazyListState.firstVisibleItemIndex
//                            Log.i("Swipe", "swiping left to ${currentIndex-1}")
//                            lazyListState.animateScrollToItem(currentIndex + 1)
//                        }
//                    }
//                }
//            },
//horizontalArrangement = Arrangement.Center,
//flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
//) {
//    itemsIndexed(photos.value) { index, photo ->
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.padding(20.dp).fillParentMaxSize().safeDrawingPadding(),
//        ) {
//            AsyncImage(
//                model = photo.uri,
//                contentDescription = null,
//                contentScale = ContentScale.Fit,
//                placeholder = painterResource(R.drawable.placeholder_gray),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .safeDrawingPadding()
//                    .padding(16.dp)
//            )
//        }
//    }
//}