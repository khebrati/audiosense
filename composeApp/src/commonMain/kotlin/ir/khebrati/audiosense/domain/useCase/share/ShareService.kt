package ir.khebrati.audiosense.domain.useCase.share

import androidx.compose.ui.graphics.ImageBitmap

interface ShareService {
    fun shareText(text: String)
    fun shareImage(bitmap: ImageBitmap)
}

