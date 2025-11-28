package ir.khebrati.audiosense.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(text: String, onDismiss: () -> Unit, onRemove: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onRemove,
                colors =
                    ButtonDefaults.buttonColors()
                        .copy(containerColor = MaterialTheme.colorScheme.errorContainer),
            ) {
                Text("Yes", color = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("No") } },
        text = { Text(text) },
    )
}
