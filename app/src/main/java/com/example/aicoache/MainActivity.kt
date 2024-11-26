package com.example.aicoache

import CustomAdapter
import Item
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.example.aicoache.databinding.MainLayoutBinding
import com.example.aicoache.databinding.ListItemBinding
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.trainingLib.AiSupport
import com.example.trainingLib.ApiResponseModel
import com.example.trainingLib.ResponseModel
import com.example.trainingLib.TrainingCourseDetail
import com.example.trainingLib.requestModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : ComponentActivity() {

    private lateinit var binding: MainLayoutBinding
    private lateinit var itemBinding: ListItemBinding

    val token : String = "eyJhbGciOiJIUzUxMiJ9.eyJhcHBfbG9naW5fdXNlcl9rZXkiOiJiY2YzYmRhNC1lNzZjLTQ4NTMtYTMxNS1iMTVkZThkNWEyMDcifQ.lsUCEiblyGYaC-PwvrTkxsk7AuaqTdmJVGL1PSAQo6dHZrumA6sOwqBmMBYuGYwAH-mTiI9ggPbGcAvsFCoszQ"
    private lateinit var listView : ListView
    private lateinit var adapter : CustomAdapter
    private val listData = mutableListOf<List<String>>()  // 用于存储数据的 List
    private lateinit var planList:List<TrainingCourseDetail> //训练计划数据

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        itemBinding = ListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initload()
        enableEdgeToEdge()
    }
    fun initload() {
        listView = binding.dataList  // 获取 ListView
        adapter = CustomAdapter(this, listData)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            println("onStart-点击了执行")
            val courseContent:String = planList[position].courseContent
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("item", courseContent)
            startActivity(intent)
        }

        showUserInfo()
        //首次进入获取计划
        getPlanBtn()
        println("onStart-执行")
        ///所有请求开线程
        ///先用天气
        binding.createPlan.setOnClickListener {
            println("创建计划")
            createPlanBtn()
        }
        binding.getPlan.setOnClickListener {
            println("获取计划")
            getPlanBtn()
        }
        binding.stopPlan.setOnClickListener {
            println("停止计划")
            stopPlanBtn()
        }
    }
    fun login() {
        println("login-执行11")
        binding.login.setOnClickListener {
            println("login-执行22")
            binding.userinfo.text = "登录成功"
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            binding.login.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
        }
    }

    fun showUserInfo() {

        var text:String = """
            出生日期-birthday:  2000-11-09,
            课程类型-courseType:  健康跑,
            身高-height:  172cm,
            近1个月跑量类型-monthlyDistanceType:  <50km,
            用户性别-sex:  男,
            课程开始时间-startTime:  2024-11-24,
            1周训练日-trainingDaysPerWeek:  周一、周三、周五,
            体重-weight:  62kg,
        """.trimIndent()
        binding.userinfo.text = text
    }
    fun createPlanBtn() {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)
        val requestModel = requestModel(
            birthday = "2000-01-01",
            courseType = 2,
            height = 175,
            monthlyDistanceType = 1,
            sex = 0,
            startTime = formattedDate,
            trainingDaysPerWeek = "1,3,5",
            weight = 70
        )
        var response : ResponseModel?
        AiSupport().createPlan(token,requestModel){ res ->
            val dataMap = Gson().fromJson(res, Map::class.java)
            val value = Gson().toJson(dataMap["data"])
            println("value=========>$value")
            response = Gson().fromJson(value, ResponseModel::class.java)
            planList = response?.trainingCourseDetailList ?: emptyList()
            runOnUiThread {
                if (dataMap["code"].toString() == "200.0") {
                    Toast.makeText(this, "创建计划成功", Toast.LENGTH_SHORT).show()
                }
                println("${planList.size}")
                clearAllItem(adapter)
                for (i in planList){
                    println(i.courseTime)
                    val dataItem = listOf(i.courseTime, i.courseName)
                    addItem(dataItem, adapter)
                }
            }
        }

    }
    fun getPlanBtn() {
        var response : ResponseModel?
        AiSupport().getPlan(token){ res ->
            val dataMap = Gson().fromJson(res, Map::class.java)
            val value = Gson().toJson(dataMap["data"])
            println("value=========>$value")
            response = Gson().fromJson(value, ResponseModel::class.java)
            planList = response?.trainingCourseDetailList ?: emptyList()
            runOnUiThread {
                if (dataMap["code"].toString() == "200.0") {
                    Toast.makeText(this, "获取计划成功", Toast.LENGTH_SHORT).show()
                }
//                println("${planList.size}")
                clearAllItem(adapter)
                for (i in planList){
                    println(i.courseTime)
                    val dataItem = listOf(i.courseTime, i.courseName)
                    addItem(dataItem, adapter)
                }
            }
        }
    }
    fun stopPlanBtn() {
        AiSupport().stopPlan(token){res->
            val map = Gson().fromJson(res, Map::class.java)
            println("map===>$map")
            runOnUiThread {
                clearAllItem(adapter)
                if (map["code"].toString() == "200.0") {
                    Toast.makeText(this, "停止计划成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 动态添加项
    private fun addItem(newItem: List<String>, adapter: CustomAdapter) {
        listData.add(newItem)
        adapter.notifyDataSetChanged()
    }
    // 删除所有项
    private fun clearAllItem(adapter: CustomAdapter) {
        listData.clear()
        adapter.notifyDataSetChanged()
    }


}
