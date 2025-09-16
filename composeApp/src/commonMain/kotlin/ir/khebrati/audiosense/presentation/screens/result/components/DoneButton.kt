package ir.khebrati.audiosense.presentation.screens.result.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private fun DoneButton(onClick: () -> Unit,modifier: Modifier = Modifier) {
    Button(modifier = modifier.fillMaxWidth().height(60.dp), onClick = onClick) {
        Text("Done")
    }
}
@Preview
@Composable
fun previewDoneButton(){
    AppTheme {
        DoneButton({})
    }
}