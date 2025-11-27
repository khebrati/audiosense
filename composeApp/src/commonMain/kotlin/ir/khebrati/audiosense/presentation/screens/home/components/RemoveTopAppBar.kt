package ir.khebrati.audiosense.presentation.screens.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveTopAppBar(
    modifier: Modifier = Modifier,
    text: String,
    onRemove: () -> Unit,
    isDelete: Boolean,
) {
    Row(
        modifier = modifier.padding(start = 25.dp, end = 25.dp)
    ) {
        TopAppBar(
            title = { Text(text = text, style = MaterialTheme.typography.titleLarge) },
            actions = {
                if (isDelete)
                    IconButton(onClick = onRemove) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete tests")
                    }
            },
        )
    }
}

@Preview
@Composable
fun PreviewRemoveTopAppBar() {
    AppTheme {
        RemoveTopAppBar(modifier = Modifier.width(1000.dp), text = 4.toString(), onRemove = {}, isDelete = true)
    }
}
