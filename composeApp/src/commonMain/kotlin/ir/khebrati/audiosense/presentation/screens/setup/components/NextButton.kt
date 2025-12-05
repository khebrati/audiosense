package ir.khebrati.audiosense.presentation.screens.setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    isDone: Boolean,
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ){
            Text(text = if(isDone) "Start" else "Next", style = MaterialTheme.typography.bodyLargeEmphasized, fontWeight = FontWeight.SemiBold)
            val imageVector = if(!isDone) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.PlayArrow
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = imageVector,
                contentDescription = "Next button"
            )
        }
    }
}

@Composable
@Preview
fun NextButtonPreview(){
    AppTheme {
        NextButton(
            enabled = true,
            isDone = false,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun DoneButtonPreview(){
    NextButton(
        enabled = true,
        isDone = true,
        onClick = {}
    )
}
