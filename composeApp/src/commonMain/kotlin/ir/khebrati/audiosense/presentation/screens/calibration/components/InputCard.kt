package ir.khebrati.audiosense.presentation.screens.calibration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InputCard(
    onValueChange: (String) -> Unit,
    title: String,
    placeHolder: @Composable (() -> Unit) = {},
    value: String = "",
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().height(130.dp).padding(21.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                TextField(
                    value = value,
                    placeholder = placeHolder,
//                    placeholder = {
//                        Text("e.g, Sony WH-1000XM4", style = MaterialTheme.typography.titleMedium)
//                    },
                    onValueChange = onValueChange,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}

@Composable
fun DeviceNameInputCard(
    onValueChange: (String) -> Unit,
    value: String = "",
) {
    InputCard(
        onValueChange = onValueChange,
        title = "Device Name",
        placeHolder = { Text("e.g, Sony WH-1000XM4", style = MaterialTheme.typography.titleMedium) },
        value = value,
    )
}

@Composable
fun MeasuredSPLInputCard(
    onValueChange: (String) -> Unit,
    value: String = "",
) {
    InputCard(
        onValueChange = onValueChange,
        title = "Measured SPL",
        placeHolder = { Text("e.g, 85 dB", style = MaterialTheme.typography.titleMedium) },
        value = value,
    )
}

@Preview
@Composable
fun DeviceNamePreview() {
    DeviceNameInputCard(
        onValueChange = {},
        value = "Sony WH-1000XM4"
    )
}
@Preview
@Composable
fun MeasuredSPLPreview() {
    MeasuredSPLInputCard(
        onValueChange = {},
        value = "85 dB"
    )
}
