package com.example.trainingLib
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

class AiSupport {
    constructor()
    companion object {
        val baseUrl = "https://test-health.cyweemotion.cn/cmms"
        val createPlanUrl = "${baseUrl}/external/trainingCourse/generateTrainingPlan"
        val getPlanUrl = "${baseUrl}/external/trainingCourse/getTrainingCourseInfo"
        val stopPlanUrl = "${baseUrl}/external/trainingCourse/stopTrainingPlan"
        val loginUrl = "${baseUrl}/external/userAccountRegister"
    }
    ///登录
    fun login(params:LoginInfoModel,callback: (String?) -> Unit){
        val gson = Gson()
        val paramsJson = gson.toJson(params)
        println(paramsJson)
        HttpClientHelper().post(loginUrl, paramsJson, callback)
    }
    ///生成计划
    fun createPlan(params:requestModel, callback: (String?) -> Unit, errCallback: (String?) -> Unit){
        var trainingDaysPerWeek: List<String> = params.trainingDaysPerWeek.split(",")
        if(params.userId == null) {
            errCallback("userId不能为空")
            return
        } else if (!(isValidDate(params.birthday))) {
            errCallback("出生日期错误")
            return
        } else if (isDateBeforeToday(params.startTime)) {
            errCallback("开始日期错误")
            return
        } else if (params.courseType < 0 || params.courseType > 6) {
            errCallback("课程类型错误")
            return
        } else if (params.height == 0) {
            errCallback("身高错误")
            return
        } else if (params.weight == 0) {
            errCallback("体重错误")
            return
        } else if (params.monthlyDistanceType < 1 || params.monthlyDistanceType > 4) {
            errCallback("近1月跑量错误")
            return
        } else if (params.sex != 0 && params.sex != 1) {
            errCallback("性别错误")
            return
        } else if (trainingDaysPerWeek.size < 2 || trainingDaysPerWeek.size > 6) {
            errCallback("1周训练日 错误")
            return
        }
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

    fun isValidDate(dateString: String): Boolean {
        // 定义日期格式
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 设置严格解析模式
        dateFormat.isLenient = false

        try {
            // 尝试解析字符串
            dateFormat.parse(dateString)
            return true
        } catch (e: Exception) {
            // 如果解析失败，返回 false
            return false
        }
    }
    fun isDateBeforeToday(dateString: String): Boolean {
        // 定义日期格式，确保和传入的字符串格式一致
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 获取当前日期
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // 解析传入的日期字符串
        try {
            val inputDate = dateFormat.parse(dateString)

            // 比较传入的日期和今天的日期
            return inputDate.before(today.time)
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果日期字符串解析失败，返回 false
            return false
        }
    }
}