package com.studysnap.ml

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OcrManager(private val context: Context) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @Suppress("DEPRECATION")
    suspend fun extractText(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri)
            ) ?: return@withContext Result.failure(Exception("Cannot decode image"))
            val rotated = fixRotation(uri, bitmap)
            val image = InputImage.fromBitmap(rotated, 0)
            val text = suspendCancellableCoroutine { cont ->
                recognizer.process(image)
                    .addOnSuccessListener { v -> cont.resume(v.text) }
                    .addOnFailureListener { e -> cont.resumeWithException(e) }
            }
            if (text.isBlank()) Result.failure(Exception("No text found"))
            else Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("DEPRECATION")
    private fun fixRotation(uri: Uri, bitmap: android.graphics.Bitmap): android.graphics.Bitmap {
        val ei = context.contentResolver.openInputStream(uri)?.use { ExifInterface(it) }
        val angle = when (ei?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        if (angle == 0) return bitmap
        val matrix = Matrix().apply { postRotate(angle.toFloat()) }
        return android.graphics.Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}