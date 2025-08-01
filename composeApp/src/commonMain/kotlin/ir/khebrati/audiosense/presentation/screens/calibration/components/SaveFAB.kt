package ir.khebrati.audiosense.presentation.screens.calibration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SaveFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.End, modifier = modifier.fillMaxWidth()) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(60.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Save",
            )
        }
    }
}
