package ir.khebrati.audiosense.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AudiosenseScaffold(
    contentPadding: PaddingValues = PaddingValues(start = 25.dp, end = 25.dp, bottom = 25.dp),
    floatingActionButton : @Composable (() -> Unit) = {},
    topBar : @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(contentPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            content()
        }
    }
}
