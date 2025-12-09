package ir.khebrati.audiosense.presentation.screens.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ir.khebrati.audiosense.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TipCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconDescription: String,
    title: String,
    body: String,
) {
    Card(
        colors =
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(modifier = modifier.padding(14.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier.clip(RoundedCornerShape(20))
                                .background(MaterialTheme.colorScheme.surface).padding(7.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = iconDescription,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(title, style = MaterialTheme.typography.titleMediumEmphasized)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(body, style = MaterialTheme.typography.bodyMediumEmphasized)
                }
            }
        }
    }
}

@Preview
@Composable
fun TipCardPreview() {
    AppTheme {
        TipCard(
            modifier = Modifier.width(300.dp),
            icon = Icons.AutoMirrored.Outlined.VolumeUp,
            iconDescription = "Volume up",
            title = "Maximize Volume",
            body = "Set your phone to 100% volume for the test to have accurate results.",
        )
    }
}
