package com.example.imagetagger.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel
import kotlin.math.absoluteValue


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

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 0.dp), // Adjust padding for preview effect
        ) { page ->

            AsyncImage(
                model = photos.value[page].uri,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.placeholder_gray),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .safeDrawingPadding()
                    .padding(16.dp)
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        shape = RoundedCornerShape(16.dp)
                        clip = true
                    }
            )

        }

    }

}