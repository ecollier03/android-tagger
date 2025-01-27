package com.example.imagetagger.composables

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.imagetagger.BigPhoto
import com.example.imagetagger.R
import com.example.imagetagger.models.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun BigPhoto(
    initialIndex: Int,
    modifier: Modifier
){
//    val viewModel: ViewModel = hiltViewModel()
//    val lazyPagingItems = viewModel.pagingData.collectAsLazyPagingItems()
//
//
//    // State for the HorizontalPager
//    val pagerState = rememberPagerState(pageCount = { lazyPagingItems.itemCount }, initialPage = initialIndex)
//    val coroutineScope = rememberCoroutineScope()
//    Log.i("page", "pager state ${pagerState.currentPage} and initial index $initialIndex")
//
//    LaunchedEffect(pagerState, initialIndex) {
//        pagerState.scrollToPage(5)
//    }
//
//
//    HorizontalPager(
//        state = pagerState,
//        modifier = modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) { pageIndex ->
//        val file = lazyPagingItems[pageIndex]
//        if (file != null) {
//            AsyncImage(
//                model = file.uri,
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                placeholder = painterResource(R.drawable.placeholder_gray),
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            )
//        } else {
//            Loading(modifier)
//        }
//    }

}