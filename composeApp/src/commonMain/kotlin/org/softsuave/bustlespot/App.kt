package org.softsuave.bustlespot


import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.compose.rememberNavController
import kotlinx.datetime.Month
import org.softsuave.bustlespot.mainnavigation.RootNavigationGraph
import org.softsuave.bustlespot.theme.AppTheme
import org.softsuave.bustlespot.utils.CustomTitleBar


@Composable
internal fun App(onFocusReceived: () -> Unit = {},
                 onDragStart: (Offset) -> Unit = { },
                 onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit = { change, dragAmount ->},
                 onMinimizeClick:() -> Unit ={},
                 onCloseClick : () -> Unit ={},) {
    val navController = rememberNavController()
    val currentPlatform = getPlatform()
    MaterialTheme {
//        Column(modifier = Modifier.fillMaxSize()
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { offset ->
////                        onDragStart(offset)
//                    },
//                    onDrag = { change, dragAmount ->
////                        onDrag(change,dragAmount)
//                    },
//                )
//            }
//        ) {
//            if(currentPlatform.name == "Desktop"){
//                CustomTitleBar(
//                    onMinimizeClick = {onMinimizeClick()},
//                    onCloseClick = {onCloseClick()}
//                )
//            }
         RootNavigationGraph(navController, onFocusReceived)
//        }
    }
}


