package com.example.imagetagger.composables

import android.util.Log
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

@Composable
fun BigPhoto(
    initialIndex: Int,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier
){
    val viewModel: ViewModel = hiltViewModel()
    val lazyPagingItems = viewModel.scrollPagingData.collectAsLazyPagingItems()

    Log.i("BigPhoto","data is ${lazyPagingItems.itemCount}")
    val pagerState = rememberPagerState(initialPage = initialIndex) { lazyPagingItems.itemCount }

    onIndexChange(pagerState.currentPage)

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { pageIndex ->
        val file = lazyPagingItems[pageIndex]
        if (file != null) {
            AsyncImage(
                model = file.uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder_gray),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        } else {
            Loading(modifier)
        }
    }

}