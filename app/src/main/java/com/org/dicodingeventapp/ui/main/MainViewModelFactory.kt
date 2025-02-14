package com.org.dicodingeventapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.local.datastore.SettingPreferences
import com.org.dicodingeventapp.ui.setting.SettingViewModel

class MainViewModelFactory  constructor(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown SettingViewModel class: " + modelClass.name)
    }
}