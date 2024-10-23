package com.example.myapplication.data.model

import com.example.myapplication.databinding.ActivityCompanyBinding
import java.io.Serializable

data class JobData(
    var id: String = "",
    var jobtitle: String = "",
    var jobdesc: String = "",
    var salary: String = "",
    var location: String = "",
    var companyId: String = ""
) : Serializable
