package com.example.emotify.View

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.emotify.R
import java.io.File

class cameraResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_result)

        val imageView = findViewById<ImageView>(R.id.imageView3)
        val textView = findViewById<TextView>(R.id.emotion_result)

        // Get the passed data
        val imagePath = intent.getStringExtra("imagePath")
        val emotion = intent.getStringExtra("emotion")

        Log.d("CameraResult", "Image path: $imagePath")
        Log.d("CameraResult", "Emotion: $emotion")

        Glide.with(this).clear(imageView)
        // Load image into ImageView using Glide
        if (imagePath != null) {
            val imageFile = File(imagePath)
            Glide.with(this)
                .load(imageFile)
                .into(imageView)
        }

        // Set detected emotion
        textView.text = emotion ?: "Emotion not detected"
    }
}
