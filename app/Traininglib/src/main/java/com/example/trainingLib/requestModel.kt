package com.example.trainingLib

data class requestModel (
    var userId:Long,
    var birthday:String,
    var courseType:Int,
    var height:Int,
    var monthlyDistanceType:Int,
    var sex:Int,
    var startTime:String,
    var trainingDaysPerWeek:String,
    var weight:Int,
){
    fun displayInfo() {
        println("birthday: $birthday")
    }

}