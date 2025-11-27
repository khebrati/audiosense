package ir.khebrati.audiosense.presentation.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Circle
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
fun SelectableCheckbox(modifier: Modifier = Modifier,selected: Boolean, onChange: (Boolean) -> Unit) {
    if (selected) {
        Icon(
            modifier = modifier.clickable { onChange(false) },
            imageVector = Icons.Default.TaskAlt,
            contentDescription = "Selected",
        )
    } else
        Icon(
            modifier = modifier.clickable { onChange(true) },
            imageVector = Icons.Outlined.Circle,
            contentDescription = "Not Selected",
        )
}

@Preview
@Composable
fun PreviewSelectableCheckbox(){
    var selected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SelectableCheckbox(Modifier,selected,{selected = it})
    }
}
