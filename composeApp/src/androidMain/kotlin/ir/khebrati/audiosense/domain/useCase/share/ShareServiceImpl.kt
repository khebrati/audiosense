package ir.khebrati.audiosense.domain.useCase.share

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

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

    override fun shareImage(bitmap: ImageBitmap) {
        val imagesDir = File(context.value.filesDir,"images")
        if(!imagesDir.exists()){
            imagesDir.mkdir()
        }
        val file = File(imagesDir,"audiogram.jpg")
        FileOutputStream(file).use {stream ->
            bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.JPEG,90,stream)
        }
        val uri = FileProvider.getUriForFile(
            context.value,
            "ir.khebrati.audiosense.fileprovider",
            file,
        )
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM,uri)
            type = "image/jpeg"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share your test results").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.value.startActivity(
            shareIntent
        )
    }
}