package com.example.myapplication.data.model

import java.io.Serializable

data class DisabilityData(
    var id: String = "",
    var name: String = "",
    var age: Int = 0,
    var phone: String = "",
    var email: String = "",
    var skill: String = "",
    var disability: String = ""
):Serializable