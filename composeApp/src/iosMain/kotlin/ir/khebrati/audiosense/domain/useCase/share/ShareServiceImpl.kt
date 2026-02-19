package ir.khebrati.audiosense.domain.useCase.share

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.*
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

class ShareServiceImpl : ShareService {

    override fun shareText(text: String) {
        val activityItems = listOf(text)
        presentActivityViewController(activityItems)
    }

    override fun shareImage(bitmap: ImageBitmap) {
        // Convert ImageBitmap to UIImage
        val uiImage = bitmap.toUIImage()
            ?: error("Failed to convert ImageBitmap to UIImage")

        val activityItems = listOf(uiImage)
        presentActivityViewController(activityItems)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun presentActivityViewController(activityItems: List<Any>) {
        val viewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        // On iPad, UIActivityViewController requires a popover presentation
        viewController.popoverPresentationController?.apply {
            val rootViewController = getRootViewController()
            sourceView = rootViewController?.view
            sourceRect = rootViewController?.view?.bounds ?: CGRectZero.readValue()
            permittedArrowDirections = 0uL // No arrow
        }

        getRootViewController()?.presentViewController(
            viewController,
            animated = true,
            completion = null
        )
    }

    private fun getRootViewController(): UIViewController? {
        return UIApplication.sharedApplication
            .connectedScenes
            .filterIsInstance<UIWindowScene>()
            .firstOrNull()
            ?.windows
            ?.filterIsInstance<UIWindow>()
            ?.firstOrNull { it.isKeyWindow() }
            ?.rootViewController
    }
}