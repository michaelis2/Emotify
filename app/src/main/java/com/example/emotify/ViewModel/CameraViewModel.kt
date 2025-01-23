package com.example.emotify.ViewModel

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel : ViewModel() {

    private val _isPermissionGranted = MutableLiveData<Boolean>()
    val isPermissionGranted: LiveData<Boolean> get() = _isPermissionGranted

    private val _photoCaptured = MutableLiveData<String>()
    val photoCaptured: LiveData<String> get() = _photoCaptured

    var imageCapture: ImageCapture? = null

    fun checkPermissions(granted: Boolean) {
        _isPermissionGranted.value = granted
    }

    fun capturePhoto(outputPath: String) {
        imageCapture?.let { capture ->
            val outputOptions = ImageCapture.OutputFileOptions.Builder(File(outputPath)).build()
            capture.takePicture(
                outputOptions,
                Dispatchers.IO.asExecutor(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        _photoCaptured.postValue(outputPath)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraViewModel", "Photo capture failed: ${exception.message}", exception)
                    }
                }
            )
        }
    }
}
