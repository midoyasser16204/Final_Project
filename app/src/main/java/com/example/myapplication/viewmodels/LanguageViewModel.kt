package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LanguageViewModel : ViewModel() {
    val languageLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setLanguage(languageCode: String) {
        languageLiveData.value = languageCode
    }
}
