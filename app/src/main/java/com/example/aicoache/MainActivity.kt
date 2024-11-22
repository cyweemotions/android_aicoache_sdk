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
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {


    private lateinit var binding: MainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        initload()
        login()
        enableEdgeToEdge()
    }
    fun initload() {
        println("onStart-执行")
        ///所有请求开线程
        ///先用天气
        thread {
            AiSupport().request()
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
}
