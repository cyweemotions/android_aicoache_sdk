package com.example.trainingLib

data class LoginInfoModel(
    val appkey:String, //appKey
    val sex:Int,//用户性别(0-男 1-女）
    val userAccount:String,//用户帐号
    val userName:String,//用户名称
    val userPassword:String,//用户密码

)
