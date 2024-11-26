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
        val baseUrl = "https://test-health.cyweemotion.cn/cmms"
        val createPlanUrl = "${baseUrl}/external/trainingCourse/generateTrainingPlan"
        val getPlanUrl = "${baseUrl}/external/trainingCourse/getTrainingCourseInfo"
        val stopPlanUrl = "${baseUrl}/external/trainingCourse/stopTrainingPlan"
        val loginUrl = "${baseUrl}/external/userAccountRegister"
    }


    fun test(){
        println("这是一个sdk打印")
    }

    ///登录
    fun login(params:LoginInfoModel,callback: (String?) -> Unit){
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        HttpClientHelper().post(loginUrl, paramsJson, callback)
    }
    ///生成计划
    fun createPlan(params:requestModel,callback: (String?) -> Unit){
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        HttpClientHelper().post(createPlanUrl, paramsJson, callback)
    }
    //获取计划
    fun getPlan(userId: Long, callback: (String?) -> Unit) {
        HttpClientHelper().get(getPlanUrl, userId){ res->
            println("回调函数处理结果res===>: $res")

            callback(res)
        }


    }
    //停止计划
    fun stopPlan(userId: Long, callback: (String?) -> Unit) {
        HttpClientHelper().stopPost(stopPlanUrl, userId, callback)
    }


    ///入参
    ///创建计划 :1.创建计划所有参数，1.检查数据的格式合法性 2.数据长度问题 3.是否存在空格 4.我们需要按照数据范围在文档中体现
    ///登录入参: 1.用户名密码长度 2.特殊字符 3.空格处理 账号: 6到20个字符之间 密码:限制6到30个字符之间

    ///出参
    ///1.错误码处理，如何显示错误信息
    ///2.模拟出空数据情况
    ///3.模拟超时情况
    ///4.模拟没网络情况或者弱网情况
    ///5.停止计划失败，再去请求获取是否能拿到。
    ///6.userid错误情况，如何处理


}