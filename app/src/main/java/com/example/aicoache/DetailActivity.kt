package com.example.aicoache

import CustomAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.aicoache.databinding.ActivityDetailBinding
import com.example.aicoache.databinding.ListItemBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var itemBinding: ListItemBinding
    private lateinit var listView : ListView
    private lateinit var adapter : CustomAdapter
    private val listData = mutableListOf<List<String>>()  // 用于存储数据的 List

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        itemBinding = ListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
//        enableEdgeToEdge()
        val detailData: String = intent.getStringExtra("item").toString()
        val planDetailList:List<String> = detailData.split("\n")
        println("detailData====>")
        println(detailData)

        listView = binding.detailDataList  // 获取 ListView
        adapter = CustomAdapter(this, listData)
        listView.adapter = adapter


        for (i in planDetailList){
            println("i====>")
            println(i)

            val planDetailListL:List<String> = i.split(":")
            val dataItem = listOf(planDetailListL[0], planDetailListL[1])
            addItem(dataItem, adapter)
        }
    }

    // 动态添加项
    private fun addItem(newItem: List<String>, adapter: CustomAdapter) {
        listData.add(newItem)
        adapter.notifyDataSetChanged()
    }
}