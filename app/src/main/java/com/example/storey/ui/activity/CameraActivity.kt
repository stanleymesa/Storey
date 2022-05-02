package com.example.storey.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.storey.R
import com.example.storey.data.local.statics.StaticData
import com.example.storey.databinding.ActivityCameraBinding
import com.example.storey.helper.createFile
import com.example.storey.helper.makeSnackbar

class CameraActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        startCameraX()
        binding.ivSwitchCamera.setOnClickListener(this)
        binding.ivCaptureCamera.setOnClickListener(this)
    }

    private fun startCameraX() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (ex: Exception) {
                resources.getString(R.string.camera_error).makeSnackbar(binding.root).show()
            }
        }, ContextCompat.getMainExecutor(this))

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(StaticData.KEY_PICTURE_RESULT, photoFile)
                    intent.putExtra(StaticData.KEY_ISBACKCAMERA,
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(AddStoryActivity.CAMERA_X_RESULT, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    resources.getString(R.string.take_picture_error).makeSnackbar(binding.root)
                        .show()
                }

            }
        )

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_switch_camera -> {
                cameraSelector = when (cameraSelector) {
                    CameraSelector.DEFAULT_BACK_CAMERA -> {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    }
                    else -> {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                }
                startCameraX()
            }
            R.id.iv_capture_camera -> {
                takePhoto()
            }
        }
    }
}