package com.example.virtualtryon.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.virtualtryon.ml.PoseDetector
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseAnalyzer(
    context: Context,
    private val onResults: (PoseLandmarkerResult?, Int, Int) -> Unit
) : ImageAnalysis.Analyzer {

    private val poseDetector = PoseDetector(context)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {

        try {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap()
            
            // Rotate bitmap to match the orientation the AI expects (upright)
            val rotatedBitmap = if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }

            val mpImage = BitmapImageBuilder(rotatedBitmap).build()
            val result = poseDetector.detect(mpImage)

            if (result != null && result.landmarks().isNotEmpty()) {
                Log.d("POSE", "Detected landmarks!")
            }

            // We pass the dimensions of the ROTATED bitmap so the overlay coordinates match
            onResults(result, rotatedBitmap.width, rotatedBitmap.height)

        } catch (e: Exception) {
            Log.e("POSE", "Error: ${e.message}")
        } finally {
            image.close()
        }
    }
}