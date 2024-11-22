package com.example.aicoache

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.aicoache.databinding.MainLayoutBinding
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.aicoache.ui.theme.AiCoacheTheme
import com.example.trainingLib.AiSupport
import com.example.trainingLib.requestModel
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {


    private lateinit var binding: MainLayoutBinding

    val token : String = "eyJhbGciOiJIUzUxMiJ9.eyJhcHBfbG9naW5fdXNlcl9rZXkiOiJiN2Y0YTBlZC1mN2MwLTQwZGEtYTI5Ni1hOWEwMDI4ZjA3NWUifQ.8xebKgfKKWOWAuo8Aai4hP7__IPcQkk1XYBHMsawvFYXWtrWr6iA08_lxCyxJZfQvgGyVeMdWeTkaZcWQjqn4w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initload()
        login()
        showUserInfo()
        enableEdgeToEdge()
    }
    fun initload() {
        println("onStart-执行")
        ///所有请求开线程
        ///先用天气
        thread {
            AiSupport().request()
        }
        binding.createPlan.setOnClickListener {
            println("创建计划")
            createPlanBtn()
        }
    }
    fun login() {
        println("login-执行11")
        binding.login.setOnClickListener {
            println("login-执行22")
            binding.userinfo.text = "登录成功"
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            binding.login.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
//            binding.login.visibility = View.GONE  // 隐藏视图
//            button.visibility = View.VISIBLE  // 显示视图
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
        val requestModel = requestModel(
            birthday = "1990-01-01",
            courseType = 2,
            height = 175,
            monthlyDistanceType = 1,
            sex = 0,
            startTime = "2024-11-01",
            trainingDaysPerWeek = "1,3",
            weight = 70
        )
        thread {
            AiSupport().createPlan(token,requestModel)
        }
    }
}
