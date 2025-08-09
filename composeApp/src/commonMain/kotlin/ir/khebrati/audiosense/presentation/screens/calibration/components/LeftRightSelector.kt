package ir.khebrati.audiosense.presentation.screens.calibration.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.domain.model.Side
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LeftRightSelector(
    selectedSide: Side,
    onSideChange: (Side) -> Unit,
    modifier: Modifier = Modifier
){
    val sides = remember {
        Side.entries.toList()
    }
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ){
        sides.forEachIndexed { index, value ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = sides.size
                ),
                label = {
                    Text(value.name, style = MaterialTheme.typography.labelLarge)
                },
                onClick = {
                    onSideChange(value)
                },
                selected = value == selectedSide,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
fun LeftRightSelectorPreview() {
    LeftRightSelector(
        selectedSide = Side.LEFT,
        onSideChange = {},
        modifier = Modifier.width(70.dp)
    )
}