package com.example.myapplication.view

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OpenAIService(private val apiKey: String) {

    private val client = OkHttpClient()

    fun sendMessage(prompt: String, callback: (String) -> Unit) {
        val url = "https://api.openai.com/v1/chat/completions"

        val json = JSONObject().apply {
            put("model", "gpt-4-turbo")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("max_tokens", 100)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val result = JSONObject(responseBody).getJSONArray("choices")
                    .getJSONObject(0).getJSONObject("message").getString("content")
                callback(result.trim())
            }
        })
    }
}
