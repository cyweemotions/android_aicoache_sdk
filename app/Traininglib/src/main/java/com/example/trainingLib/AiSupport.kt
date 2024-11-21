package com.example.trainingLib
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class AiSupport {
    constructor(

    )

    fun test(){
        println("这是一个sdk打印")
    }

    fun request(){
        val client = OkHttpClient()
        // 创建 GET 请求
        val request = Request.Builder().get()
            .url("https://jsonplaceholder.typicode.com/posts") // 示例 URL
            .build()
        // 执行请求
        val response: Response = client.newCall(request).execute()
        // 处理响应
        if (response.isSuccessful) {
            println("Response: ${response.body?.string()}")

        } else {
            println("Request failed: ${response.code}")
        }
    }
}