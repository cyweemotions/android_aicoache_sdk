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
    companion object {
        val createPlanUrl = "https://test-health.cyweemotion.cn/cmms/app/trainingCourse/generateTrainingPlan"
        val getPlanUrl = "https://test-health.cyweemotion.cn/cmms/app/trainingCourse/getTrainingCourseInfo"
        val stopPlanUrl = "https://test-health.cyweemotion.cn/cmms/app/trainingCourse/stopTrainingPlan"
    }


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

    ///生成计划
    fun createPlan(token:String, params:requestModel) : String? {
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        val response = HttpClientHelper().post(createPlanUrl, paramsJson, token)

        println("创建计划===》response : ${response.toString()}")
        return response
    }

    //获取计划
    fun getPlan(token:String) : ResponseModel? {
        val response = HttpClientHelper().get(getPlanUrl, token=token)
        val res = Gson().fromJson(response, ResponseModel::class.java)
        println("获取计划===》response : ${response.toString()}")
        return res
    }

    //停止计划
    fun stopPlan(token:String, params:requestModel) :String {
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        val response = HttpClientHelper().delete(stopPlanUrl, paramsJson, token)
        return ""
    }





}