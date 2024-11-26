package com.example.trainingLib


import okhttp3.*
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import okhttp3.Response

class HttpClientHelper {


    private val client: OkHttpClient
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor() // 创建单线程执行器

    init {
        client = OkHttpClient.Builder()
            .addInterceptor(LogInterceptor()) // 自定义日志拦截器
            .build()
    }

    // GET 请求
    fun get(url: String, userId: Long, callback: (String?) -> Unit) {
        println("GET 请求===>${userId}")
        //addHeader("userId", userId)
        val requestUrl : String = "$url?userId=$userId"
        val request = Request.Builder().url(requestUrl).build()
        executeRequest(request, callback)
    }

    // POST 请求
    fun post(url: String, json: String,callback: (String?) -> Unit) {
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
        val request = Request.Builder().post(body).url(url).build()
        executeRequest(request, callback)
    }
    fun stopPost(url: String, userId: Long, callback: (String?) -> Unit) {
        println("stopPost 请求===>${userId}")
        val requestUrl : String = "$url?userId=$userId"
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), "")
        val request = Request.Builder().post(body).url(requestUrl).build()
        executeRequest(request, callback)
    }

    // DELETE 请求
    fun delete(url: String, callback: (String?) -> Unit) {
        val request = Request.Builder().url(url).delete().build()
        executeRequest(request, callback)
    }

    // 执行请求，返回结果
    private fun executeRequest(request: Request, callback: (String?) -> Unit) {
        // 在子线程中执行网络请求
        executorService.submit {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    callback(response.body?.string())
                } else {
                    callback("Error: ${response.code}")
                }
            } catch (e: IOException) {
                callback("Request failed: ${e.message}")
            }
        }
    }

    // 自定义日志拦截器
    class LogInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val requestLog = StringBuilder("Request: ${request.method} ${request.url}\n")
            println(requestLog.toString()) // 打印请求信息

            // 执行请求，获取响应
            val response = chain.proceed(request)

            // 读取响应体内容
            val responseBodyString = response.body?.string()

            // 打印响应信息
            val responseLog = StringBuilder("Response: ${response.code} ${response.message}\n")
            responseLog.append("Response Body: $responseBodyString\n")

            println(responseLog.toString()) // 打印响应信息

            // 为了避免流已关闭问题，重新构建一个新的响应体
            val newResponseBody = responseBodyString?.let { ResponseBody.create(response.body?.contentType(), it) }
            return response.newBuilder().body(newResponseBody).build() // 返回一个新的 Response
        }
    }
}
