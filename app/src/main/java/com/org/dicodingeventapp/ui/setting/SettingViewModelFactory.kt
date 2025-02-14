package com.org.dicodingeventapp.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.local.datastore.SettingPreferences
import com.org.dicodingeventapp.ui.home.HomeViewModelFactory

class SettingViewModelFactory  constructor(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown SettingViewModel class: " + modelClass.name)
    }
}