package org.softsuave.bustlespot.auth.di

import android.app.Activity
import android.media.projection.MediaProjection
import androidx.activity.ComponentActivity
import com.company.app.screenshot.ScreenshotProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.softsuave.bustlespot.MediaProjectionHolder
import org.softsuave.bustlespot.accessability.AccessibilityPermission


actual val platformModule: Module = org.koin.dsl.module {
    single<MediaProjection> {
        MediaProjectionHolder.mediaProjection
            ?: error("MediaProjection not available. Request permissi on first.")
    }
    factory {
        ScreenshotProvider(androidContext(), get())
    }
    factory { (activity: Activity) -> activity }
    single<AccessibilityPermission> {
        AccessibilityPermission()
    }
    factory { (activity: ComponentActivity) -> activity }
}