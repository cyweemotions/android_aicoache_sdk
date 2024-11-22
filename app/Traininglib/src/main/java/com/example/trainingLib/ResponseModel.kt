package com.example.trainingLib

data class ResponseModel(
    val title:String,
    val body:String,
    val id:Int,
    val userId:Int,
    val arrary:ArrayList<String>
){
    fun displayInfo() {
        println("title: $title")
    }

}