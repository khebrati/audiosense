package ir.khebrati.audiosense.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import audiosense.composeapp.generated.resources.Lexend_Black
import audiosense.composeapp.generated.resources.Lexend_Bold
import audiosense.composeapp.generated.resources.Lexend_ExtraBold
import audiosense.composeapp.generated.resources.Lexend_ExtraLight
import audiosense.composeapp.generated.resources.Lexend_Light
import audiosense.composeapp.generated.resources.Lexend_Medium
import audiosense.composeapp.generated.resources.Lexend_Regular
import audiosense.composeapp.generated.resources.Lexend_SemiBold
import audiosense.composeapp.generated.resources.Lexend_Thin
import audiosense.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


@Composable
fun bodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.Lexend_Light, weight = FontWeight.Light),
    Font(Res.font.Lexend_ExtraLight, weight = FontWeight.ExtraLight),
    Font(Res.font.Lexend_Regular, weight = FontWeight.Normal),
    Font(Res.font.Lexend_Medium, weight = FontWeight.Medium),
    Font(Res.font.Lexend_ExtraBold, weight = FontWeight.ExtraBold),
    Font(Res.font.Lexend_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Lexend_Bold, weight = FontWeight.Bold),
    Font(Res.font.Lexend_Black, weight = FontWeight.Black),
    Font(Res.font.Lexend_Thin, weight = FontWeight.Thin),
)


@Composable
fun displayFontFamily(): FontFamily = FontFamily(
    Font(Res.font.Lexend_Light, weight = FontWeight.Light),
    Font(Res.font.Lexend_ExtraLight, weight = FontWeight.ExtraLight),
    Font(Res.font.Lexend_Regular, weight = FontWeight.Normal),
    Font(Res.font.Lexend_Medium, weight = FontWeight.Medium),
    Font(Res.font.Lexend_ExtraBold, weight = FontWeight.ExtraBold),
    Font(Res.font.Lexend_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Lexend_Bold, weight = FontWeight.Bold),
    Font(Res.font.Lexend_Black, weight = FontWeight.Black),
    Font(Res.font.Lexend_Thin, weight = FontWeight.Thin),
)

// Default Material 3 typography values
val baseline = Typography()

@Composable
fun AppTypography() = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily()),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily()),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily()),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily()),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily()),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily()),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily()),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily()),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily()),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily()),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily()),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily()),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily()),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily()),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily()),
)

