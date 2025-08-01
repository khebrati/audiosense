package ir.khebrati.audiosense.presentation.screens.calibration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FrequencyCard(
    frequency: Int,
    frequencies: List<Int>,
    onFreqChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FREQUENCY", style = MaterialTheme.typography.labelLargeEmphasized)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = frequency.toString(),
                    style = MaterialTheme.typography.displayLargeEmphasized,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row {
                Slider(
                    steps = frequencies.size - 2,
                    valueRange = 0f..frequencies.size.toFloat() - 1,
                    onValueChange = onFreqChange,
                    value = frequencies.indexOf(frequency).toFloat(),
                )
            }
        }
    }
}

@Preview
@Composable
internal fun FrequencyCardPreview() {
    val frequency = remember { mutableStateOf(1000) }
    val frequencies = listOf(125, 250, 500, 1000, 2000, 4000, 8000)
    AppTheme {
        FrequencyCard(
            frequency = frequency.value,
            frequencies = frequencies,
            onFreqChange = {
                frequency.value = frequencies[it.toInt()]
            },
            modifier = Modifier.height(200.dp)
        )
    }
}
