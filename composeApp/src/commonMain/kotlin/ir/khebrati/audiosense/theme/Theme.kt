// Generated using MaterialKolor Builder version 1.2.1 (103)
// https://materialkolor.com/?color_seed=FF00B9F1&color_primary=FF00B9F1&color_secondary=FFC3C1FF&color_tertiary=FF0084F4&color_error=FFB3261E&color_neutral=FF1C1B1F&color_neutralvariant=FF757575&dark_mode=false&style=Expressive&contrast=-1.0&color_spec=SPEC_2025&package_name=com.example.app&misc=true&expressive=true

package ir.khebrati.audiosense.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

private val reducedContrastLightColorScheme = lightColorScheme(
    primary = PrimaryLightReducedContrast,
    onPrimary = OnPrimaryLightReducedContrast,
    primaryContainer = PrimaryContainerLightReducedContrast,
    onPrimaryContainer = OnPrimaryContainerLightReducedContrast,
    inversePrimary = InversePrimaryLightReducedContrast,
    secondary = SecondaryLightReducedContrast,
    onSecondary = OnSecondaryLightReducedContrast,
    secondaryContainer = SecondaryContainerLightReducedContrast,
    onSecondaryContainer = OnSecondaryContainerLightReducedContrast,
    tertiary = TertiaryLightReducedContrast,
    onTertiary = OnTertiaryLightReducedContrast,
    tertiaryContainer = TertiaryContainerLightReducedContrast,
    onTertiaryContainer = OnTertiaryContainerLightReducedContrast,
    background = BackgroundLightReducedContrast,
    onBackground = OnBackgroundLightReducedContrast,
    surface = SurfaceLightReducedContrast,
    onSurface = OnSurfaceLightReducedContrast,
    surfaceVariant = SurfaceVariantLightReducedContrast,
    onSurfaceVariant = OnSurfaceVariantLightReducedContrast,
    surfaceTint = SurfaceTintLightReducedContrast,
    inverseSurface = InverseSurfaceLightReducedContrast,
    inverseOnSurface = InverseOnSurfaceLightReducedContrast,
    error = ErrorLightReducedContrast,
    onError = OnErrorLightReducedContrast,
    errorContainer = ErrorContainerLightReducedContrast,
    onErrorContainer = OnErrorContainerLightReducedContrast,
    outline = OutlineLightReducedContrast,
    outlineVariant = OutlineVariantLightReducedContrast,
    scrim = ScrimLightReducedContrast,
    surfaceBright = SurfaceBrightLightReducedContrast,
    surfaceContainer = SurfaceContainerLightReducedContrast,
    surfaceContainerHigh = SurfaceContainerHighLightReducedContrast,
    surfaceContainerHighest = SurfaceContainerHighestLightReducedContrast,
    surfaceContainerLow = SurfaceContainerLowLightReducedContrast,
    surfaceContainerLowest = SurfaceContainerLowestLightReducedContrast,
    surfaceDim = SurfaceDimLightReducedContrast,
    primaryFixed = PrimaryFixedReducedContrast,
    primaryFixedDim = PrimaryFixedDimReducedContrast,
    onPrimaryFixed = OnPrimaryFixedReducedContrast,
    onPrimaryFixedVariant = OnPrimaryFixedVariantReducedContrast,
    secondaryFixed = SecondaryFixedReducedContrast,
    secondaryFixedDim = SecondaryFixedDimReducedContrast,
    onSecondaryFixed = OnSecondaryFixedReducedContrast,
    onSecondaryFixedVariant = OnSecondaryFixedVariantReducedContrast,
    tertiaryFixed = TertiaryFixedReducedContrast,
    tertiaryFixedDim = TertiaryFixedDimReducedContrast,
    onTertiaryFixed = OnTertiaryFixedReducedContrast,
    onTertiaryFixedVariant = OnTertiaryFixedVariantReducedContrast,
)

private val reducedContrastDarkColorScheme = darkColorScheme(
    primary = PrimaryDarkReducedContrast,
    onPrimary = OnPrimaryDarkReducedContrast,
    primaryContainer = PrimaryContainerDarkReducedContrast,
    onPrimaryContainer = OnPrimaryContainerDarkReducedContrast,
    inversePrimary = InversePrimaryDarkReducedContrast,
    secondary = SecondaryDarkReducedContrast,
    onSecondary = OnSecondaryDarkReducedContrast,
    secondaryContainer = SecondaryContainerDarkReducedContrast,
    onSecondaryContainer = OnSecondaryContainerDarkReducedContrast,
    tertiary = TertiaryDarkReducedContrast,
    onTertiary = OnTertiaryDarkReducedContrast,
    tertiaryContainer = TertiaryContainerDarkReducedContrast,
    onTertiaryContainer = OnTertiaryContainerDarkReducedContrast,
    background = BackgroundDarkReducedContrast,
    onBackground = OnBackgroundDarkReducedContrast,
    surface = SurfaceDarkReducedContrast,
    onSurface = OnSurfaceDarkReducedContrast,
    surfaceVariant = SurfaceVariantDarkReducedContrast,
    onSurfaceVariant = OnSurfaceVariantDarkReducedContrast,
    surfaceTint = SurfaceTintDarkReducedContrast,
    inverseSurface = InverseSurfaceDarkReducedContrast,
    inverseOnSurface = InverseOnSurfaceDarkReducedContrast,
    error = ErrorDarkReducedContrast,
    onError = OnErrorDarkReducedContrast,
    errorContainer = ErrorContainerDarkReducedContrast,
    onErrorContainer = OnErrorContainerDarkReducedContrast,
    outline = OutlineDarkReducedContrast,
    outlineVariant = OutlineVariantDarkReducedContrast,
    scrim = ScrimDarkReducedContrast,
    surfaceBright = SurfaceBrightDarkReducedContrast,
    surfaceContainer = SurfaceContainerDarkReducedContrast,
    surfaceContainerHigh = SurfaceContainerHighDarkReducedContrast,
    surfaceContainerHighest = SurfaceContainerHighestDarkReducedContrast,
    surfaceContainerLow = SurfaceContainerLowDarkReducedContrast,
    surfaceContainerLowest = SurfaceContainerLowestDarkReducedContrast,
    surfaceDim = SurfaceDimDarkReducedContrast,
    primaryFixed = PrimaryFixedReducedContrast,
    primaryFixedDim = PrimaryFixedDimReducedContrast,
    onPrimaryFixed = OnPrimaryFixedReducedContrast,
    onPrimaryFixedVariant = OnPrimaryFixedVariantReducedContrast,
    secondaryFixed = SecondaryFixedReducedContrast,
    secondaryFixedDim = SecondaryFixedDimReducedContrast,
    onSecondaryFixed = OnSecondaryFixedReducedContrast,
    onSecondaryFixedVariant = OnSecondaryFixedVariantReducedContrast,
    tertiaryFixed = TertiaryFixedReducedContrast,
    tertiaryFixedDim = TertiaryFixedDimReducedContrast,
    onTertiaryFixed = OnTertiaryFixedReducedContrast,
    onTertiaryFixedVariant = OnTertiaryFixedVariantReducedContrast,
)

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AppTheme(
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember(systemIsDark) { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        SystemAppearance(!isDark)
        MaterialExpressiveTheme(
            colorScheme = if (isDark) reducedContrastDarkColorScheme else reducedContrastLightColorScheme,
            motionScheme = MotionScheme.expressive(),
            typography =AppTypography() ,
            content = { Surface(content = content) }
        )
    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
