package ir.khebrati.audiosense.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Earbuds
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HeadphoneIcon(modelName: String,modifier: Modifier = Modifier) {
    val icon =
        if (isEarbuds(modelName)) Icons.Default.Earbuds else Icons.Default.Headphones
    Icon(
        contentDescription = "Headphone icon",
        imageVector = icon,
        modifier = modifier
    )
}

private fun isEarbuds(name: String) = name.lowercase().contains(Regex("bud|buds|airpod|airpods"))
