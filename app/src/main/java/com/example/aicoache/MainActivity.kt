package com.example.aicoache

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import butterknife.BindView
import butterknife.ButterKnife
import com.example.aicoache.ui.theme.AiCoacheTheme
import com.example.trainingLib.AiSupport
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
//    @BindView(R.id.login)
    lateinit var btn:Button
//    var username: EditText? = null
//    @BindView(R.id.button)
//    lateinit var button: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        ButterKnife.bind(this)
        login()
//        initload()
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
//        binding.login.setOnClickListener {
//            println("login-执行22")
//            binding.userinfo.text = "登录成功"
//            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
//            binding.login.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
//            binding.login.visibility = View.GONE  // 隐藏视图
////          button.visibility = View.VISIBLE  // 显示视图
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AiCoacheTheme {
        Greeting("Android")
    }
}