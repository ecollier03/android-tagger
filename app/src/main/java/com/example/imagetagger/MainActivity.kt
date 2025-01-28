package com.example.imagetagger

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.imagetagger.models.ViewModel
import com.example.imagetagger.composables.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf


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
                dynamicColor && darkTheme -> {
                    Log.i("Color Scheme", "Dynamic Dark")
                    dynamicDarkColorScheme(LocalContext.current)
                }
                dynamicColor && !darkTheme -> {
                    Log.i("Color Scheme", "Dynamic Light")
                    dynamicLightColorScheme(LocalContext.current)
                }
                darkTheme -> {
                    Log.i("Color Scheme", "Dark")
                    darkColorScheme()
                }
                else -> {
                    Log.i("Color Scheme", "Light")
                    lightColorScheme()
                }
            }
            MaterialTheme(
                colorScheme = colors
            ) {
                MainEntry()
            }
        }
    }
}


@Serializable
object StartScreen

@Serializable
object PhotoScreen

@Serializable
object BigPhoto

@SuppressLint("MissingPermission")
@Composable
fun MainEntry(viewModel: ViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var currentImageIndex by remember { mutableIntStateOf(0) }

    val backHandler: (destination: Any) -> Unit = { destination ->
        when (destination) {
            PhotoScreen -> {
                navController.navigate(PhotoScreen) {
                    popUpTo(PhotoScreen) { inclusive = false }
                }
            }
            BigPhoto -> {
                navController.navigate(BigPhoto) {
                    popUpTo(BigPhoto) { inclusive = false }
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
            MainPhotoScreen(
                index = currentImageIndex,
                onPhotoClick = {
                    currentImageIndex = it
                    backHandler(BigPhoto)
                }
            )
        }
        composable<BigPhoto> {
            ImageCarousel(
                initialIndex = currentImageIndex,
                onPageChange = {
                    currentImageIndex = it
                },
                modifier = Modifier
            )
        }
    }
}