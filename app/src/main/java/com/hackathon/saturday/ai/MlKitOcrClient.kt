package com.hackathon.saturday.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitOcrClient(context: Context) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @Suppress("DEPRECATION")
    fun processImage(uri: Uri): String {
        val bitmap = BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(uri)
        ) ?: return ""
        val rotated = fixRotation(context, uri, bitmap)
        val image = InputImage.fromBitmap(rotated, 0)
        return suspendCancellableCoroutine { continuation ->
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    continuation.resume(visionText.text)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

    @Suppress("DEPRECATION")
    private fun fixRotation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val ei = context.contentResolver.openInputStream(uri)?.use {
            ExifInterface(it)
        }
        val orientation = ei?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        ) ?: ExifInterface.ORIENTATION_NORMAL
        val angle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        return if (angle != 0) {
            val matrix = Matrix()
            matrix.postRotate(angle.toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else bitmap
    }
}
