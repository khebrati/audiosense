package ir.khebrati.audiosense.domain.useCase.share

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

class ShareServiceImpl(
    val context: Lazy<android.content.Context>
) : ShareService {
    override fun shareText(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/json"
            putExtra(Intent.EXTRA_TEXT,text)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share your test results").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.value.startActivity(
            shareIntent
        )
    }
}