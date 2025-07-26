package ir.khebrati.audiosense

import androidx.compose.runtime.*
import ir.khebrati.audiosense.data.source.dao.HeadphoneDao
import ir.khebrati.audiosense.data.source.entity.LocalHeadphone
import ir.khebrati.audiosense.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.Koin

@Preview
@Composable
internal fun App(koin: Koin) = AppTheme {
}
