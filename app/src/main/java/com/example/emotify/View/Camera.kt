package com.example.emotify.View

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.emotify.R
import com.example.emotify.ViewModel.CameraViewModel
import java.nio.ByteBuffer

class Camera : Fragment() {

    private lateinit var cameraProviderFuture: ProcessCameraProvider
    private lateinit var viewFinder: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraViewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_camera, container, false)
        viewFinder = rootView.findViewById(R.id.viewFinder)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)

        // Request camera permission and setup the button listener
        requestCameraPermission()
        view.findViewById<Button>(R.id.image_capture_button).setOnClickListener {
            captureAndUploadImage()
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 1001)
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext()).get()

        // Preview setup
        val preview = androidx.camera.core.Preview.Builder().build().apply {
            setSurfaceProvider(viewFinder.surfaceProvider)
        }

        // ImageCapture setup
        imageCapture = ImageCapture.Builder().build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Bind preview and image capture use cases
            cameraProviderFuture.unbindAll()
            cameraProviderFuture.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            Log.e("CameraX", "Use case binding failed", e)
        }
    }

    private fun captureAndUploadImage() {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val buffer = image.planes[0].buffer
                    val bytes = bufferToByteArray(buffer)

                    // Upload the ByteArray directly to Firebase
                    cameraViewModel.uploadImageToDatabase(
                        bytes,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            Toast.makeText(requireContext(), "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )

                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Image capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun bufferToByteArray(buffer: ByteBuffer): ByteArray {
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return bytes
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
