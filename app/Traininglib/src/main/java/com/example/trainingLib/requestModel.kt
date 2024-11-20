package com.example.trainingLib

data class requestModel (
    val birthday:String,
    val courseType:Int,
    val height:Int,
    val monthlyDistanceType:Int,
    val sex:Int,
    val startTime:String,
    val trainingDaysPerWeek:String,
    val weight:Int,
){
    fun displayInfo() {
        println("birthday: $birthday")
    }

}