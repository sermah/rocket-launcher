package com.sermah.rocketlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.sermah.rocketlauncher.data.AppInfo
import com.sermah.rocketlauncher.repository.AppsRepo
import com.sermah.rocketlauncher.ui.theme.RocketLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appsList = remember { AppsRepo.loadAppsList(applicationContext) }
            val listState = LazyListState()

            RocketLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Rocket Launcher")
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = listState,
                        ) {
                            itemsIndexed(items = appsList) { _, appInfo ->
                                AppItem(appInfo = appInfo, Modifier.fillMaxWidth()) {
                                    appInfo.launch(applicationContext)
                                }
                            }
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
    onClick: () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ) {
        Image(
            rememberDrawablePainter(drawable = appInfo.icon),
            "${appInfo.label} app icon",
            modifier = Modifier.size(48.dp)
        )
        Text(appInfo.label)
    }
}