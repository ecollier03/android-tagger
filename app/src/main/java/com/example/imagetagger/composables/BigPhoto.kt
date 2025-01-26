package com.example.imagetagger.composables

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.imagetagger.BigPhoto
import com.example.imagetagger.R


@Composable
fun BigPhoto(uri: Uri?, modifier: Modifier){
    if(uri != null) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder_gray),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }

    Text(
        text = "Hello I Am text",
        modifier = Modifier
    )
}