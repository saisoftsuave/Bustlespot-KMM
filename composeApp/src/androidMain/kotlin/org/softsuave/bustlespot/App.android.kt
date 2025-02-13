package org.softsuave.bustlespot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.softsuave.bustlespot.di.initKoin
import org.softsuave.bustlespot.screenshot.takeScreenShot
import org.softsuave.bustlespot.ui.RequestMediaProjectionPermissionButton
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger


class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initKoin {
            androidLogger()
            androidContext(this@AppActivity)
        }
        setContent {
            App()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                RequestMediaProjectionPermissionButton()
            }
        }
    }

}

@Preview
@Composable
fun AppPreview() {
    App()
}
