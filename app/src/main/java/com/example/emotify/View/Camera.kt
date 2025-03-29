package com.example.emotify.View

import android.content.Intent
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Fragment responsible for handling camera operations.
 * Captures images using CameraX, sends them to a Flask API for emotion detection,
 * and navigates to the result screen displaying the detected emotion.
 */

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
       // startRealTimeEmotionDetection()
        view.findViewById<Button>(R.id.image_capture_button).setOnClickListener {
            captureAndUploadImage()


        }
    }

    /**
     * Requests camera permission and starts the camera if granted.
     */
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

    /**
     * Initializes and starts the camera preview.
     */
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

    /**
     * Captures an image and uploads it to the Flask API.
     */
    private fun captureAndUploadImage() {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val buffer = image.planes[0].buffer
                    val bytes = bufferToByteArray(buffer)

                    val timestamp = System.currentTimeMillis()
                    // Create a temporary file for storing the captured image
                    val tempFile = File(requireContext().cacheDir, "capturedimage_$timestamp.jpg")

                    if (tempFile.exists()) {
                        tempFile.delete()
                    }

                    tempFile.writeBytes(bytes)

                    // Send the image to the Flask API
                    sendImageToFlaskAPI(tempFile)

                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Image capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    /**
     * Converts a ByteBuffer to a ByteArray.
     */
    private fun bufferToByteArray(buffer: ByteBuffer): ByteArray {
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return bytes
    }

    /**
     * Sends the captured image to the Flask API for emotion detection.
     */
    private fun sendImageToFlaskAPI(imageFile: File)
    {
        val client = OkHttpClient()

        val mediaType = "image/jpeg".toMediaTypeOrNull()
        val fileRequestBody = RequestBody.create(mediaType, imageFile)
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", imageFile.name, fileRequestBody)
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/predict") //The url set on the Flask API server and the predict function on it for emotion prediction
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to connect to API: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val responseString = responseBody.string()

                        // Parse the JSON response
                        val json = JSONObject(responseString)
                        val emotion = json.getString("emotion")

                        println("Detected Emotion: $emotion")
                        // Navigate to the cameraResult activity with the image path and emotion
                        val intent = Intent(requireContext(), cameraResult::class.java)
                        intent.putExtra("imagePath", imageFile.absolutePath)
                        intent.putExtra("emotion", emotion)
                        requireActivity().runOnUiThread {
                            startActivity(intent)
                        }
                    }
                } else {
                    println("Server returned an error: ${response.code}")
                }
            }
        })

            }


    /**
     * Handles permission request results.
     */
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
