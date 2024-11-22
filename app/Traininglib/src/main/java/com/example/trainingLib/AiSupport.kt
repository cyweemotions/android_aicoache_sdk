package com.example.trainingLib
import android.annotation.SuppressLint
import android.provider.ContactsContract.Data
import com.google.gson.Gson
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

    @SuppressLint("SuspiciousIndentation")
    fun request(){
        val response = HttpClientHelper().get("https://jsonplaceholder.typicode.com/posts")
//        val client = OkHttpClient()
//        // 创建 GET 请求
//        val request = Request.Builder().get()
//            .url("https://jsonplaceholder.typicode.com/posts") // 示例 URL
//            .build()
//        // 执行请求
//        val response: Response = client.newCall(request).execute()
//        // 处理响应
//        if (response.isSuccessful) {
//            val res = Gson().fromJson(response.body?.string(), Array<ResponseModel>::class.java).toMutableList()
//            for(value in res){
//                println("Response: ${value.id}")
//                println("Response: ${value.title}")
//                println("Response: ${value.userId}")
//                println("Response: ${value.body}")
//            }
//        } else {
            println("Requestsdsd : ${response.toString()}")
//        }


    }





}