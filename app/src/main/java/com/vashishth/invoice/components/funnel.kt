package com.vashishth.invoice.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview


fun drawFunnel(upperRadius: Float, lowerRadius: Float, width: Float): Path {
    return Path().apply {
        // Top arc
        arcTo(
            rect = Rect(
                left = -lowerRadius,
                top = -upperRadius - lowerRadius,
                right = width * 2 - lowerRadius,
                bottom = upperRadius - lowerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        // Bottom arc
        arcTo(
            rect = Rect(
                left = -lowerRadius,
                top = upperRadius + lowerRadius,
                right = width * 2 - lowerRadius,
                bottom = upperRadius * 3 + lowerRadius
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        close()
    }
}
