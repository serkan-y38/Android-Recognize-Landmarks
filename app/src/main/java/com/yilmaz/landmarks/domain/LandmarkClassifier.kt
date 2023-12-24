package com.yilmaz.landmarks.domain

import android.graphics.Bitmap
import com.yilmaz.landmarks.domain.ClassificationModel

interface LandmarkClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<ClassificationModel>
}