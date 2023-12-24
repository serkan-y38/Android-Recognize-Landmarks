package com.yilmaz.landmarks.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yilmaz.landmarks.data.LandmarkClassifier
import com.yilmaz.landmarks.domain.ClassificationModel
import com.yilmaz.landmarks.presentation.ui.theme.LandmarksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 0
            )
        }

        setContent {
            LandmarksTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var classificationModels by remember {
                        mutableStateOf(emptyList<ClassificationModel>())
                    }
                    val analyzer = remember {
                        LandmarkImageAnalyzer(
                            classifier = LandmarkClassifier(
                                context = applicationContext
                            ),
                            onResults = {
                                classificationModels = it
                            }
                        )
                    }
                    val controller = remember {
                        LifecycleCameraController(applicationContext).apply {
                            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                            setImageAnalysisAnalyzer(
                                ContextCompat.getMainExecutor(applicationContext),
                                analyzer
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CameraPreview(controller, Modifier.fillMaxSize())
                        Text(
                            "",
                            modifier = Modifier
                                .width(321.dp)
                                .height(321.dp)
                                .padding(10.dp)
                                .align(Alignment.Center)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .padding(16.dp, 16.dp)
                                .wrapContentHeight()
                                .wrapContentHeight()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .align(alignment = Alignment.BottomCenter),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            classificationModels.forEach {
                                Text(
                                    it.name,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

}