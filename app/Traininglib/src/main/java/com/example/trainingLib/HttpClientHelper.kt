package com.example.trainingLib


import okhttp3.*
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response

class HttpClientHelper {

    private val client: OkHttpClient

    init {
        // 创建 OkHttpClient，添加请求拦截器
        client = OkHttpClient.Builder()
            .addInterceptor(LogInterceptor()) // 自定义日志拦截器
            .build()
    }

    // GET 请求
    fun get(url: String): String? {
        val request = Request.Builder().url(url).build()
        return executeRequest(request)
    }

    // POST 请求
    fun post(url: String, json: String): String? {
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
        val request = Request.Builder().url(url).post(body).build()
        return executeRequest(request)
    }

    // DELETE 请求
    fun delete(url: String): String? {
        val request = Request.Builder().url(url).delete().build()
        return executeRequest(request)
    }

    // 执行请求，返回结果
    private fun executeRequest(request: Request): String? {
        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()
            } else {
                "Error: ${response.code}"
            }
        } catch (e: IOException) {
            "Request failed: ${e.message}"
        }
    }

    // 自定义日志拦截器
    class LogInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val requestBody = request.body
            val requestLog = StringBuilder("Request: ${request.method} ${request.url}\n")
            if (requestBody != null) {
                requestLog.append("====================Request Body====================: \n${requestBody.toString()}\n")
            }

            println(requestLog.toString()) // 打印请求信息

            // 执行请求，获取响应
            val response = chain.proceed(request)

            // 读取响应体内容
            val responseBodyString = response.body?.string()

            // 打印响应信息
            val responseLog = StringBuilder("Response: ${response.code} ${response.message}\n")
            responseLog.append("====================Response Body====================: \n$responseBodyString\n")

            println(responseLog.toString()) // 打印响应信息

            // 为了避免流已关闭问题，重新构建一个新的响应体
            val newResponseBody = responseBodyString?.let { ResponseBody.create(response.body?.contentType(), it) }
            return response.newBuilder().body(newResponseBody).build() // 返回一个新的 Response
        }
    }
}
