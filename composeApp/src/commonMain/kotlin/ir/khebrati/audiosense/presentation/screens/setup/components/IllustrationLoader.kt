package ir.khebrati.audiosense.presentation.screens.setup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import audiosense.composeapp.generated.resources.Res
import co.touchlab.kermit.Logger
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.*
import coil3.compose.AsyncImagePainter.State.*
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import ir.khebrati.audiosense.presentation.components.LoadingScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun IllustrationLoader(
    modifier: Modifier = Modifier,
    illustrationName: String,
    content: @Composable () -> Unit,
){
    var loading by remember { mutableStateOf(true) }
    val painter =
        rememberAsyncImagePainter(
            model = Res.getUri("drawable/$illustrationName.png"),
            onState = { state ->
                if (state is Success) {
                    loading = false
                }
            },
        )
    if (loading) {
        LoadingScreen()
    } else {
        ReadyIllustration(painter,content, modifier)
    }
}

@Composable
private fun ReadyIllustration(painter: AsyncImagePainter, content: @Composable () -> Unit,modifier: Modifier) {
    Column {
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(15))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = 70.dp)
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = "Test setup illustration",
                modifier = modifier
            )
        }
        content()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun IllustrationLoaderPreview(){
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.Red.toArgb())
    }
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler){
//        IllustrationLoader()
    }
}