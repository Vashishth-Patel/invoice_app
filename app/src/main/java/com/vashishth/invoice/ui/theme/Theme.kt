package com.vashishth.invoice.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = blue10,
    onPrimary = blue50,
    primaryContainer = blue40,
    onPrimaryContainer = blue0,
    inversePrimary = bluelight,
    secondary = darkblue10,
    onSecondary = darkblue50,
    secondaryContainer = darkblue40,
    onSecondaryContainer = darkblue10,
    tertiary = peel10,
    onTertiary = peel50,
    tertiaryContainer = peel40,
    onTertiaryContainer = peel10,
    error = Color.Red,
    background = Color.Black,
    onBackground = Color.Black,
    surface = Color.Black,
    onSurface = blue20,
    inverseSurface = blue0,
    inverseOnSurface = blue50
)

private val LightColorPalette = lightColorScheme(
    primary = blue10,
    onPrimary = blue50,
    primaryContainer = blue40,
    onPrimaryContainer = blue0,
    inversePrimary = bluelight,
    secondary = darkblue10,
    onSecondary = darkblue50,
    secondaryContainer = darkblue40,
    onSecondaryContainer = darkblue10,
    tertiary = peel10,
    onTertiary = peel50,
    tertiaryContainer = peel40,
    onTertiaryContainer = peel10,
    error = Color.Red,
    background = Color.Black,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = blue20,
    inverseSurface = blue0,
    inverseOnSurface = blue50
)

@Composable
fun InvoiceTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val useDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val systemUiController = rememberSystemUiController()
    val colors = if(darkTheme) {
        systemUiController.setSystemBarsColor(
            color = Color.Black
        )
        DarkColorPalette
    }else{
        systemUiController.setSystemBarsColor(
            color = Color.White
        )
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}