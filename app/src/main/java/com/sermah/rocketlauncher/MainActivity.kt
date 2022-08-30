package com.sermah.rocketlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.sermah.rocketlauncher.data.AppInfo
import com.sermah.rocketlauncher.repository.AppsRepo
import com.sermah.rocketlauncher.ui.theme.RocketLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppsRepo.loadAppsList(applicationContext)

        setContent {
            val appsList = AppsRepo.apps
            val listState = rememberLazyListState()

            RocketLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Rocket Launcher", Modifier.fillMaxWidth().padding(4.dp),
                            textAlign = TextAlign.Center)
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = listState,
                        ) {
                            itemsIndexed(items = appsList) { _, appInfo ->
                                AppItem(appInfo = appInfo, Modifier.fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 0.dp)
                                ) { appInfo.launch(applicationContext) }
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
            style = MaterialTheme.typography.body1.plus(
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
            )
        ))
    }
}