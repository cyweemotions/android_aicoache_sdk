package com.example.trainingLib

data class ResponseModel(
    val title:String,
    val body:String,
    val id:Int,
    val userId:Int,
){
    fun displayInfo() {
        println("title: $title")
    }

}