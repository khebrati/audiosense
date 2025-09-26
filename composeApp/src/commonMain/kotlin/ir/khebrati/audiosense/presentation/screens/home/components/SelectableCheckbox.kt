package ir.khebrati.audiosense.presentation.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectableCheckbox() {
    var selected by remember { mutableStateOf(false) }
    val size = 50.dp
    if (selected) {
        Icon(
            modifier = Modifier.size(size).clickable { selected = false },
            imageVector = Icons.Default.CheckBox,
            contentDescription = "Not Selected",
        )
    } else
        Icon(
            modifier = Modifier.size(size).clickable { selected = true },
            imageVector = Icons.Default.CheckBoxOutlineBlank,
            contentDescription = "Not Selected",
        )
}

@Preview
@Composable
fun PreviewSelectableCheckbox(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SelectableCheckbox()
    }
}
