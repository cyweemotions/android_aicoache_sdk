package com.example.aicoache

import CustomAdapter
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.example.aicoache.databinding.MainLayoutBinding
import com.example.aicoache.databinding.ListItemBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.lxj.xpopup.XPopup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var listener: () -> Unit
    private lateinit var binding: MainLayoutBinding
    private lateinit var itemBinding: ListItemBinding

    val appKey : String = "XXDZyRfXVepBLYk4jAZbVGL9FDdLFpfV"
    var userId : Long = 0
    private lateinit var listView : ListView
    private lateinit var adapter : CustomAdapter
    private val listData = mutableListOf<List<String>>()  // 用于存储数据的 List
    private lateinit var planList:List<TrainingCourseDetail> //训练计划数据
    private var weeks:ArrayList<Int> = arrayListOf(1,3,5)
    private var courseValue:Int = 1
    private var weight:Int = 50
    private var height:Int = 170
    private var distance:Int = 1
    private var sex:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)
        binding.startTime.text = formattedDate

//        showUserInfo()
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

    //出生日期
    fun birthdayLinearClick(view: View) {
        showDateTimePicker("请选择出生日期", opration = { value ->
            val date = Date(value)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = formatter.format(date)
            binding.birthday.text = formattedDate.toString()
        })
    }
    //课程类型
    fun courseTypeLinearClick(view: View) {
        val datas = arrayOf("健康跑","三公里", "五公里", "十公里", "半马","全马")
        showcenterListDialog(datas,opration = { value ->
            courseValue = value+1
            binding.courseType.text = datas[value.toInt()]
        })

    }
    //身高
    fun heightLinearClick(view: View) {
        var datas = arrayListOf("60cm")
        var heights = 60
        for (i in 1..30){
            heights= heights +5
            datas.add(heights.toString()+"cm")
        }
        showBottomListDialog(datas.toTypedArray(),opration = { value ->
            binding.height.text = datas[value.toInt()]
            height = binding.height.text.split("c").first().toInt()
        })
    }
    //体重
    fun weightLinearClick(view: View) {
        var datas = arrayListOf("30kg")
        var weights = 30
        for (i in 1..70){
            weights= weights +1
            datas.add(weights.toString()+"kg")
        }
        showBottomListDialog(datas.toTypedArray(),opration = { value ->
            binding.weight.text = datas[value.toInt()]
            weight = binding.weight.text.split("k").first().toInt()
        })
    }
    //近一个月跑量
    fun monthlyDistanceTypeLinearClick(view: View) {
        val datas = arrayOf("<50km ","51-150km", "151-300km", ">300km")
        showBottomListDialog(datas,opration = { value ->
            binding.monthlyDistanceType.text = datas[value.toInt()]
            distance = value.toInt() + 1
        })
    }
    //性别
    fun sexLinearClick(view: View) {
        val datas = arrayOf("男","女")
        showBottomListDialog(datas,opration = { value ->
            binding.sex.text = datas[value.toInt()]
            sex = value.toInt()
        })
    }
    //课程开始时间
    fun startTimeLinearClick(view: View) {
        showDateTimePicker("请选择课程开始时间",opration = { value ->
            val date = Date(value)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = formatter.format(date)
            binding.startTime.text = formattedDate.toString()
        })
    }
    //一周训练日
    fun strainingDaysPerWeekLinear(view: View) {
        val items = arrayOf("周一","周二","周三","周四","周五","周六","周日")
        // 使用示例
        val selectedItems = booleanArrayOf(true, false, true,false, true, false,false) // 初始选中状态
        showMultiChoiceDialog(items, selectedItems, "选择一周训练日") { selected ->
            // 处理选中结果
            println("训练日:${selected.withIndex().count()}")
            var reWeek: ArrayList<Int> = arrayListOf()
            var reweekStrings: ArrayList<String> = arrayListOf()
            for ((index, isSelected) in selected.withIndex()) {
                if (isSelected) {
                    println("选项${index + 1} 被选中")
                    reWeek.add(index + 1)
                    reweekStrings.add(items[index])
                }
            }
            if (reWeek.count() < 2 || reWeek.count() > 6) {
                Toast.makeText(this, "1周训练日 2-6天", Toast.LENGTH_SHORT).show()
            } else {
                println("选中的周:${reweekStrings}--${reWeek.toString()}")
                binding.trainingDaysPerWeek.text = reweekStrings.joinToString().replace(" ", "")
                weeks = reWeek
            }
        }
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
        AiSupport().login(loginInfoModel, callback = { res ->
            val gson = Gson()
            val mapType = object : TypeToken<Map<String, String>>() {}.type
            val dataMap: Map<String, String> = gson.fromJson(res, mapType)
            val value = gson.fromJson(dataMap["data"], Long::class.java)
            runOnUiThread {
                if (dataMap["code"] == "200") {
                    userId = value
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    binding.login.text = "登录成功"
                    binding.login.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
                }
            }
        }, errCallback = { err->
            runOnUiThread {
                println("err=========>$err")
                Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
            }
        })
    }
    ///创建计划
    fun createPlanBtn() {
        if(userId.toInt() == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        val requestModel = requestModel(
            userId = userId,
            birthday = binding.birthday.text.toString(),
            courseType = courseValue,
            height = height,
            monthlyDistanceType = distance,
            sex = sex,
            startTime = binding.startTime.text.toString(),
            trainingDaysPerWeek = weeks.joinToString().replace(" ",""),
            weight = weight
        )
        println("createPlanBtn-requestModel:${requestModel.toString()}")
        var response : ResponseModel?
        AiSupport().createPlan(requestModel, callback = { res ->
            val dataMap = Gson().fromJson(res, Map::class.java)
            runOnUiThread {
                var code = dataMap["code"] as Double
                if (code.toInt() == 200) {
                    Toast.makeText(this, "创建计划成功", Toast.LENGTH_SHORT).show()
                    val value = Gson().toJson(dataMap["data"])
                    response = Gson().fromJson(value, ResponseModel::class.java)
                    planList = response?.trainingCourseDetailList ?: emptyList()
                    println("${planList.size}")
                    clearAllItem(adapter)
                    for (i in planList){
                        println(i.courseTime)
                        val dataItem = listOf(i.courseTime, i.courseName)
                        addItem(dataItem, adapter)
                    }
                }else{
                    Toast.makeText(this, dataMap["msg"].toString()+":"+code.toInt().toString(), Toast.LENGTH_SHORT).show()
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
        if(userId.toInt() == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        var response : ResponseModel?
        AiSupport().getPlan(userId){ res ->
            val dataMap = Gson().fromJson(res, Map::class.java)
            var code = dataMap["code"] as Double
            runOnUiThread {
                if (code.toInt() == 200) {
                    Toast.makeText(this, "获取计划成功", Toast.LENGTH_SHORT).show()
                    val value = Gson().toJson(dataMap["data"])
                    println("value=========>$value")
                    response = Gson().fromJson(value, ResponseModel::class.java)
                    planList = response?.trainingCourseDetailList ?: emptyList()
                    clearAllItem(adapter)
                    for (i in planList){
                        println(i.courseTime)
                        val dataItem = listOf(i.courseTime, i.courseName)
                        addItem(dataItem, adapter)
                    }
                }else{
                    Toast.makeText(this, dataMap["msg"].toString()+":"+code.toInt().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ///停止计划
    fun stopPlanBtn() {
        if(userId.toInt() == 0) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        AiSupport().stopPlan(userId){res->
            val dataMap = Gson().fromJson(res, Map::class.java)
            val code = dataMap["code"] as Double
            println("dataMap===>$dataMap")
            runOnUiThread {
                if (code.toInt() == 200) {
                    clearAllItem(adapter)
                    Toast.makeText(this, "停止计划成功", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, dataMap["msg"].toString()+":"+code.toInt().toString(), Toast.LENGTH_SHORT).show()
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


    private fun showDateTimePicker( title: String, opration: (position: Long) -> Unit) {
        var displayList: MutableList<Int> = mutableListOf()
        displayList.add(DateTimeConfig.YEAR)
        displayList.add(DateTimeConfig.MONTH)
        displayList.add(DateTimeConfig.DAY)
        // 创建日期时间选择器实例
        CardDatePickerDialog.builder(this)
            .setTitle(title)
            .setDefaultTime(0)
            .setWrapSelectorWheel(false)
            .setChooseDateModel(DateTimeConfig.DATE_LUNAR)
            .setTouchHideable(true)
            .setDisplayType(displayList)//显示
            .setOnChoose {
                println("值:${it}")
                opration(it)
            }.build().show()
    }


    fun showBottomListDialog(datas: Array<String>,opration: (position: Int) -> Unit) {
        XPopup.Builder(this)
            .maxHeight(1200)
            .asBottomList("选择",  datas, { position, text ->
                // 点击确认按钮后的回调
                opration(position)
            })
            .show()
    }


    fun showcenterListDialog(datas: Array<String>,opration: (position: Int) -> Unit) {
        XPopup.Builder(this)
            .asCenterList("选择",  datas, { position, text ->
                // 点击确认按钮后的回调
                opration(position)
            })
            .show()
    }

    fun multiSelect(datas: Array<String>,opration: (position: Int) -> Unit){
        XPopup.Builder(this)
            .autoDismiss(false)
            .asCenterList("选择",  datas,null,1, { position, text ->
                // 点击确认按钮后的回调
                opration(position)
            })
            .show()

    }


    fun showMultiChoiceDialog(
        items: Array<String>,
        selectedItems: BooleanArray,
        title: CharSequence,
        onSubmit: (BooleanArray) -> Unit) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMultiChoiceItems(items, selectedItems) { dialog, which, isChecked ->
            // 更新选中状态
            selectedItems[which] = isChecked
        }
        dialog.setPositiveButton("确定") { dialog, which ->
            // 提交选中结果
            onSubmit(selectedItems)
        }
        dialog.setNegativeButton("取消", null)
        dialog.show()
    }

}
