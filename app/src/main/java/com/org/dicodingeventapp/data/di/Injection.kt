package com.org.dicodingeventapp.data.di

import android.content.Context
import com.org.dicodingeventapp.data.local.room.EventDatabase
import com.org.dicodingeventapp.data.remote.retrofit.ApiConfig
import com.org.dicodingeventapp.data.remote.retrofit.ApiService
import com.org.dicodingeventapp.data.repository.EventRepository
import com.org.dicodingeventapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository{
        val apiService =  ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return  EventRepository.getInstance(apiService, dao, appExecutors)
    }
}