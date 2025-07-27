package com.example.myapplication.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var openAIService: OpenAIService
    private lateinit var tvChatOutput: TextView
    private lateinit var etInput: EditText
    private lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvChatOutput = findViewById(R.id.tvChatOutput)
        etInput = findViewById(R.id.etInput)
        btnSend = findViewById(R.id.btnSend)

        openAIService = OpenAIService("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")

        btnSend.setOnClickListener {
            val inputText = etInput.text.toString().trim()
            if (inputText.isNotEmpty()) {
                tvChatOutput.append("\nYou: $inputText")

                openAIService.sendMessage(inputText) { response ->
                    runOnUiThread {
                        tvChatOutput.append("\nBot: $response\n")
                        etInput.text.clear()
                    }
                }
            }
        }

    }
}
