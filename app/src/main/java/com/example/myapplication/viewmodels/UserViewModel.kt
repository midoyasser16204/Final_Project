package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.UserData

class UserViewModel : ViewModel() {
    // LiveData to hold user data
    var Uid: String? = null
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    // Function to set or update user data
    fun setUserData(newUserData: UserData) {
        _userData.value = newUserData
    }

    // Function to update individual fields
    fun updateUserFields(uid: String? = null, email: String? = null, password: String? = null,
                         name: String? = null, phone: String? = null, address: String? = null) {
        val currentData = _userData.value ?: UserData()
        _userData.value = currentData.copy(
            uid = uid ?: currentData.uid,
            email = email ?: currentData.email,
            password = password ?: currentData.password,
            name = name ?: currentData.name,
            phone = phone ?: currentData.phone,
            address = address ?: currentData.address
        )
    }
}
