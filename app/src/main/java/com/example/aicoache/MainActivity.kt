package com.example.aicoache

import CustomAdapter
import Item
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.aicoache.databinding.MainLayoutBinding
import com.example.aicoache.databinding.ListItemBinding
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.trainingLib.AiSupport
import com.example.trainingLib.LoginInfoModel
import com.example.trainingLib.ResponseModel
import com.example.trainingLib.TrainingCourseDetail
import com.example.trainingLib.requestModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimePicker
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.lxj.xpopup.XPopup
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainLayoutBinding
    private lateinit var itemBinding: ListItemBinding

    val appKey : String = "XXDZyRfXVepBLYk4jAZbVGL9FDdLFpfV"
    var userId : Long = 0
    private lateinit var listView : ListView
    private lateinit var adapter : CustomAdapter
    private val listData = mutableListOf<List<String>>()  // 用于存储数据的 List
    private lateinit var planList:List<TrainingCourseDetail> //训练计划数据

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        itemBinding = ListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
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

//        showUserInfo()
        //首次进入获取计划
        getPlanBtn()
        println("onStart-执行")
        ///所有请求开线程
        ///先用天气
        binding.login.setOnClickListener {
            println("登录")
            login()
        }
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
//    fun showUserInfo() {
//
//        var text:String = """
//            出生日期:  2000-01-01,
//            课程类型:  健康跑,
//            身高:  175cm,
//            近1个月跑量类型:  <50km,
//            用户性别:  男,
//            课程开始时间:  2024-11-24,
//            1周训练日:  周一、周三、周五,
//            体重:  70kg,
//        """.trimIndent()
//        binding.userinfo.text = text
//    }

    //出生日期
    fun birthdayLinearClick(view: View) {
        println("你好 kotlin")
    }
    //课程类型
    fun courseTypeLinearClick(view: View) {
//        view.findViewById<TextView>(R.id.courseType).text = "全马跑"
        binding.courseType.text = "半马跑"
        println("你好 kotlin")
    }
    //身高
    fun heightLinearClick(view: View) {
        println("你好 kotlin")
    }
    //体重
    fun weightLinearClick(view: View) {
        println("你好 kotlin")
    }
    //近一个月跑量
    fun monthlyDistanceTypeLinearClick(view: View) {
        println("你好 kotlin")
    }
    //性别
    fun sexLinearClick(view: View) {
        println("你好 kotlin")
    }
    //课程开始时间
    fun startTimeLinearClick(view: View) {
        println("你好 kotlin")
    }
    //一周训练日
    fun strainingDaysPerWeekLinear(view: View) {
        println("你好 kotlin")
    }
    ///登录
    fun login() {
        if(userId.toInt() != 0) {
            Toast.makeText(this, "已经登录过了", Toast.LENGTH_SHORT).show()
            return
        }
        val loginInfoModel = LoginInfoModel(
            appkey = appKey,
            sex = 0,
            userAccount = "Test123456",
            userName = "Test",
            userPassword = "123456",
        )
        AiSupport().login(loginInfoModel){ res ->
            val gson = Gson()
            val mapType = object : TypeToken<Map<String, String>>() {}.type
            val dataMap: Map<String, String> = gson.fromJson(res, mapType)
            val value = gson.fromJson(dataMap["data"], Long::class.java)
            println("dataMap=========>$dataMap")
            println("value=========>$value")
            runOnUiThread {
                if (dataMap["code"] == "200") {
                    userId = value
                    println("userId===>$userId")
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    binding.login.text = "登录成功"
                    binding.login.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
                }
            }
        }
    }
    ///创建计划
    fun createPlanBtn() {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)
        val requestModel = requestModel(
            userId = userId,
            birthday = "2000-01-11",
            courseType = 1,
            height = 175,
            monthlyDistanceType = 1,
            sex = 0,
            startTime = formattedDate,
            trainingDaysPerWeek = "1,3,5",
            weight = 70
        )
        var response : ResponseModel?
        AiSupport().createPlan(requestModel, callback = { res ->
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
        }, errCallback = { err->
            runOnUiThread {
                println("err=========>$err")
                Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
            }
        })
    }
    ///获取计划
    fun getPlanBtn() {
        println("userId=====>$userId")
        var response : ResponseModel?
        AiSupport().getPlan(userId){ res ->
            val dataMap = Gson().fromJson(res, Map::class.java)
            val value = Gson().toJson(dataMap["data"])
            println("value=========>$value")
            response = Gson().fromJson(value, ResponseModel::class.java)
            planList = response?.trainingCourseDetailList ?: emptyList()
            runOnUiThread {
                if (dataMap["code"] == "200") {
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
        showBottomListDialog()
    }

    ///停止计划
    fun stopPlanBtn() {
        AiSupport().stopPlan(userId){res->
            val map = Gson().fromJson(res, Map::class.java)
            println("map===>$map")
            runOnUiThread {
                if (map["code"].toString() == "200.0") {
                    clearAllItem(adapter)
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


    private fun showDateTimePicker() {
        var displayList: MutableList<Int> = mutableListOf()
        displayList.add(DateTimeConfig.YEAR)
        displayList.add(DateTimeConfig.MONTH)
        displayList.add(DateTimeConfig.DAY)
        // 创建日期时间选择器实例
        CardDatePickerDialog.builder(this)
            .setTitle("设置出生日期")
            .setDefaultTime(0)
            .setWrapSelectorWheel(false)
            .setChooseDateModel(DateTimeConfig.DATE_LUNAR)
            .setTouchHideable(true)
            .setDisplayType(displayList)//显示
            .setOnChoose {
                println("值:${it}")
            }.build().show()
    }


    fun showBottomListDialog() {
        val datas = arrayOf("条目1", "条目2", "条目3", "条目4")
        XPopup.Builder(this)
            .asBottomList("提示",  datas, { position, text ->
                // 点击确认按钮后的回调
                println("Confirm clicked!")
            })
            .show()
    }

    fun multiSelect(){

    }

}
