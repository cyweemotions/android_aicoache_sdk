package com.example.trainingLib



data class ResponseModel(
    val courseType: Int,
    val endTime: String,
    val id: Long,
    val startTime: String,
    val trainingCourseDetailList: List<TrainingCourseDetail>
)

data class TrainingCourseDetail(
    val courseContent: String,
    val courseMins: Int,
    val courseName: String,
    val courseTime: String,
    val id: Long
)

data class ApiResponseModel(
    val code: Int,
    val msg: String,
    val data: ResponseModel,
)