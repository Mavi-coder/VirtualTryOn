package com.example.virtualtryon.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
    onResults: (PoseLandmarkerResult?, Int, Int) -> Unit
) {

    val cameraProviderFuture =
        ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({

        val cameraProvider =
            cameraProviderFuture.get()

        val preview =
            Preview.Builder().build()
        val imageAnalysis =
            ImageAnalysis.Builder()
                .setBackpressureStrategy(
                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                )
                // Remove explicit format to let CameraX use YUV which is more efficient for toBitmap()
                .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            PoseAnalyzer(context, onResults)
        )

        preview.surfaceProvider =
            previewView.surfaceProvider

        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )

    }, ContextCompat.getMainExecutor(context))
}