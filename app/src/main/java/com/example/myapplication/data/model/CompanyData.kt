package com.example.myapplication.data.model

import java.io.Serializable

data class CompanyData(
    var companyName: String = "",
    var phone: String = "",
    var email: String = "",
    var location: String = "",
    var websiteUrl: String = "",
    var description: String = "",
    var isComapny:Boolean=true
) : Serializable
