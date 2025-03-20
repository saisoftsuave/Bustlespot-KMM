package org.softsuave.bustlespot.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import bustlespot.composeapp.generated.resources.Res
import bustlespot.composeapp.generated.resources.ic_cancel_mui
import bustlespot.composeapp.generated.resources.ic_minimize_mui
import bustlespot.composeapp.generated.resources.logoWhite
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomTitleBar(
    modifier: Modifier = Modifier,
    title: String = "BUSTLESPOT",
    onMinimizeClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.logoWhite),
            contentDescription = "Bustlespot Icon",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.W300,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        // Minimize Button with Hover Effect
        HoverableIconButton(
            iconRes = Res.drawable.ic_minimize_mui,
            contentDescription = "Minimize",
            onClick = onMinimizeClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Close Button with Hover Effect
        HoverableIconButton(
            iconRes = Res.drawable.ic_cancel_mui,
            contentDescription = "Close",
            onClick = onCloseClick
        )
    }
}

@Composable
fun HoverableIconButton(
    iconRes: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val scale by animateFloatAsState(if (isHovered) 1.2f else 1f, label = "hover-scale")
    val size by animateDpAsState(if (isHovered) 23.dp else 21.dp, label = "hover-scale")

    IconButton(
        modifier = Modifier
            .size(25.dp)
            .hoverable(interactionSource),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(size)
                .graphicsLayer(scaleX = scale, scaleY = scale),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = Color.White,
        )
    }
}
