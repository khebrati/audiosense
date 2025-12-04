package ir.khebrati.audiosense.presentation.screens.testPreparation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import audiosense.composeapp.generated.resources.Res
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import ir.khebrati.audiosense.presentation.components.AudiosenseAppBar
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.components.LoadingScreen
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TestSetupLayout(
    modifier: Modifier = Modifier,
    title: String,
    illustrationName: String,
    onNavigateBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    var loading by remember { mutableStateOf(true) }
    val painter =
        rememberAsyncImagePainter(
            model = Res.getUri("drawable/$illustrationName.png"),
            onState = { state ->
                if (state is AsyncImagePainter.State.Success) {
                    loading = false
                }
            },
        )
    if (loading) {
        LoadingScreen()
    } else {
        TestSetupReady(modifier, title, onNavigateBack, painter, content)
    }
}

@Composable
private fun TestSetupReady(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateBack: () -> Unit,
    painter: AsyncImagePainter,
    content: @Composable () -> Unit,
) {
    AudiosenseScaffold(
        contentPadding = PaddingValues(),
        topBar = {
            AudiosenseAppBar(title = title, canNavigateBack = true, onNavigateBack = onNavigateBack)
        },
        bottomBar = {
            TestSetupBottomBar(
                modifier = Modifier.height(120.dp).fillMaxWidth(),
                onClick = {},
                count = 4,
            )
        },
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Box(
                modifier =
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer).padding(horizontal = 70.dp)
            ) {
                IllustrationLoader(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter
                )
            }
            content()
        }
    }
}

@Preview(widthDp = 400, heightDp = 800)
@Composable
fun TestSetupLayoutPreview() {
    AppTheme {
        Surface {
            TestSetupLayout(
                title = "Ready for test",
                onNavigateBack = {},
                illustrationName = "Question",
            ) {}
        }
    }
}
