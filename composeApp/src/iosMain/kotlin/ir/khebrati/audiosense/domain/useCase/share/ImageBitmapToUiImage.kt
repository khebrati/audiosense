package ir.khebrati.audiosense.domain.useCase.share

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    val pixelMap = this.toPixelMap()
    val width = pixelMap.width
    val height = pixelMap.height

    // Build a raw RGBA byte array from the pixel map
    val rawBytes = ByteArray(width * height * 4)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = pixelMap[x, y]
            val index = (y * width + x) * 4
            rawBytes[index + 0] = (color.red * 255).toInt().toByte()
            rawBytes[index + 1] = (color.green * 255).toInt().toByte()
            rawBytes[index + 2] = (color.blue * 255).toInt().toByte()
            rawBytes[index + 3] = (color.alpha * 255).toInt().toByte()
        }
    }

    return memScoped {
        val colorSpace = CGColorSpaceCreateDeviceRGB()
        val bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value

        val dataProvider = rawBytes.usePinned { pinned ->
            CGDataProviderCreateWithData(
                info = null,
                data = pinned.addressOf(0),
                size = rawBytes.size.toULong(),
                releaseData = null
            )
        }

        val cgImage = CGImageCreate(
            width = width.toULong(),
            height = height.toULong(),
            bitsPerComponent = 8u,
            bitsPerPixel = 32u,
            bytesPerRow = (width * 4).toULong(),
            space = colorSpace,
            bitmapInfo = bitmapInfo,
            provider = dataProvider,
            decode = null,
            shouldInterpolate = true,
            intent = CGColorRenderingIntent.kCGRenderingIntentDefault
        )

        cgImage?.let { UIImage.imageWithCGImage(it) }
    }
}