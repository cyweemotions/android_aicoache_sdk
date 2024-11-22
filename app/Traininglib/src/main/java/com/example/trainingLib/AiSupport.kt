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

    ///生成计划
    fun createPlan(token:String, params:requestModel,callback: (String?) -> Unit){
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        HttpClientHelper().post(createPlanUrl, paramsJson, token,callback)
    }
    //获取计划
    fun getPlanUrl(token:String, params:requestModel,callback: (String?) -> Unit) {
        val gson = Gson()
        HttpClientHelper().get(getPlanUrl, token,callback)

    }
    //停止计划
    fun stopPlanUrl(token:String, params:requestModel,callback: (String?) -> Unit) {
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        HttpClientHelper().post(stopPlanUrl, paramsJson, token,callback)
    }





}