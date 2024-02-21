package com.example.currency.data

class ConvertResponse(
    val success :Boolean =false,
    val result :String,
)

class LatestResponse(
    val success :Boolean =false,
    val rates :Map<String,Double>,
    val error:ApiError?=null
)

class ApiError(private val type :String){
    override fun toString()=type
}