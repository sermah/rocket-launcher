package com.sermah.rocketlauncher

import android.Manifest
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sermah.rocketlauncher.data.AppInfo
import com.sermah.rocketlauncher.repository.AppsManager
import com.sermah.rocketlauncher.repository.WallpaperManager
import com.sermah.rocketlauncher.ui.theme.RocketLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppsManager.loadAppsList(applicationContext)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppContent(context = applicationContext)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppContent(context: Context) {
    with (context) {
        fun loadWP() { if (!WallpaperManager.isWallpaperLoaded) WallpaperManager.loadWallpaper(this) }

        val extReadPermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
        LaunchedEffect(Unit) { extReadPermissionState.launchPermissionRequest() }
        val wallpaper: Drawable? = remember(extReadPermissionState.status.isGranted) {
            if (extReadPermissionState.status.isGranted) {
                loadWP()
                WallpaperManager.wallpaper
            } else null
        }

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            isNavigationBarContrastEnforced = true
        )

        val appsList = AppsManager.apps
        val listState = rememberLazyListState()

        RocketLauncherTheme {
            val textColor = remember(WallpaperManager.isWallpaperDark) {
                if (WallpaperManager.isWallpaperDark) Color.White
                else Color.Black
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                if (WallpaperManager.isWallpaperLoaded)
                    Image(
                        painter = rememberDrawablePainter(drawable = wallpaper),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    Text("Rocket Launcher",
                        style = TextStyle(color = textColor),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = listState,
                    ) {
                        itemsIndexed(items = appsList) { i, appInfo ->
                            AppItem(appInfo = appInfo,
                                style = TextStyle(
                                    color = textColor,
                                    shadow = Shadow(blurRadius = 2f,
                                        offset = Offset(0f, 2f)
                                    ),
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 0.dp)
                                    .then(
                                        if (i == appsList.size - 1) Modifier.navigationBarsPadding()
                                        else Modifier
                                    ),
                            ) { appInfo.launch(applicationContext) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppItem(
    appInfo: AppInfo,
    modifier: Modifier = Modifier,
    style: TextStyle,
    onClick: () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Image(
            rememberDrawablePainter(drawable = appInfo.icon),
            "${appInfo.label} app icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(appInfo.label, maxLines = 1,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
            ).plus(style)
        )
    }
}