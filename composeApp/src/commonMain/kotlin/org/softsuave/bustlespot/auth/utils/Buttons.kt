package org.softsuave.bustlespot.auth.utils

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.softsuave.bustlespot.utils.BustleSpotRed

@Composable
fun PrimaryButton(
    buttonText: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val containerColors = if (isLoading) {
        ButtonDefaults.buttonColors(
            containerColor = BustleSpotRed,
            contentColor = Color.White,
            disabledContainerColor = BustleSpotRed,
        )
    } else {
        ButtonDefaults.buttonColors().copy(
            containerColor = BustleSpotRed,
            contentColor = Color.White
        )
    }
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .focusable(enabled = true)
            .then(
                if (enabled) {
                    Modifier.pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = containerColors,
        enabled = enabled,
        interactionSource = MutableInteractionSource()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(35.dp)
            )
        } else {
            Text(
                text = buttonText,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
