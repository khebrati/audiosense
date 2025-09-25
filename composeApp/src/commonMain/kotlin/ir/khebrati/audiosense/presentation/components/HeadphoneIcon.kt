package ir.khebrati.audiosense.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Earbuds
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun HeadphoneIcon(modelName: String) {
    val icon =
        if (isEarbuds(modelName)) Icons.Default.Earbuds else Icons.Default.Headphones
    Icon(
        contentDescription = "Headphone icon",
        imageVector = icon,
        tint = MaterialTheme.colorScheme.onTertiaryContainer,
    )
}

private fun isEarbuds(name: String) = name.lowercase().contains(Regex("bud|buds|airpod|airpods"))
