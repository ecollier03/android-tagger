package com.example.imagetagger.composables

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onEnterPressed : () -> Unit
){
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        READ_MEDIA_IMAGES
    } else {
        READ_EXTERNAL_STORAGE
    }

    var errorText by remember { mutableStateOf("") }

    val permissionState = rememberMultiplePermissionsState(permissions = listOf(permission)) { map ->
        val rejectedPermissions = map.filterValues { !it }.keys
        errorText = if (rejectedPermissions.any { it in listOf(permission) }) {
            "Please grant access to device photos"
        } else {
            ""
        }
    }
    val allRequiredPermissionsGranted =
        permissionState.revokedPermissions.none { it.permission in listOf(permission) }

    if (!allRequiredPermissionsGranted) {
        Button(
            onClick = {
                permissionState.launchMultiplePermissionRequest()
                onEnterPressed()
            },
            modifier = modifier,
        ) {
            Text("Grant Photo Permissions")
        }
    }
    else {
        onEnterPressed()
    }
}

