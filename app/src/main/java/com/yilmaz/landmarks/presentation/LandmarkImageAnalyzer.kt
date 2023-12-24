package com.yilmaz.landmarks.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.yilmaz.landmarks.domain.ClassificationModel
import com.yilmaz.landmarks.domain.LandmarkClassifier
import com.yilmaz.landmarks.presentation.centerCrop

class LandmarkImageAnalyzer(
    private val classifier: LandmarkClassifier,
    private val onResults: (List<ClassificationModel>) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0)
            onResults(
                classifier.classify(
                    image.toBitmap().centerCrop(321, 321),
                    image.imageInfo.rotationDegrees
                )
            )
        frameSkipCounter++
        image.close()
    }

}