package ir.khebrati.audiosense.presentation.screens.testPreparation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    TextButton(
        onClick = onClick,
    ){
        Text("Skip to test", style = MaterialTheme.typography.bodyLargeEmphasized)
    }
}

@Preview
@Composable
fun SkipButtonPreview(){
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer
    ){
        SkipButton(
            modifier = Modifier.height(50.dp)
        )
    }
}