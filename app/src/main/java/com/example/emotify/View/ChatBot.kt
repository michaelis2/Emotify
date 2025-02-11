package com.example.emotify.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emotify.R
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.emotify.ViewModel.OpenAIApi
import com.example.emotify.ViewModel.OpenAIRequest
import com.example.emotify.ViewModel.OpenAIResponse
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ChatBot : AppCompatActivity() {

    private lateinit var userInput: EditText
    private lateinit var chatResponse: TextView
    private lateinit var sendButton: Button
    private lateinit var api: OpenAIApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        userInput = findViewById(R.id.userInput)
        chatResponse = findViewById(R.id.chatResponse)
        sendButton = findViewById(R.id.sendButton)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        api = retrofit.create(OpenAIApi::class.java)

        sendButton.setOnClickListener {
            val inputText = userInput.text.toString()
            if (inputText.isNotBlank()) {
                getChatbotResponse(inputText)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getChatbotResponse(prompt: String) {
        val request = OpenAIRequest(prompt = prompt)

        api.getResponse(request).enqueue(object : Callback<OpenAIResponse> {
            override fun onResponse(call: Call<OpenAIResponse>, response: Response<OpenAIResponse>) {
                println("Response code: ${response.code()}")
                println("Error body: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    val chatbotText = response.body()?.choices?.firstOrNull()?.text?.trim()
                    chatResponse.text = chatbotText ?: "No response from chatbot"
                } else {
                    Toast.makeText(this@ChatBot, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
                println("Error: ${t.message}")
                Toast.makeText(this@ChatBot, "Failed to connect to OpenAI", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

