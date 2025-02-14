package com.org.dicodingeventapp.ui.upcoming

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.repository.EventRepository
import com.org.dicodingeventapp.ui.home.HomeViewModel

class UpcomingViewModelFactory private constructor(private val eventRepository: EventRepository) : 
        ViewModelProvider.NewInstanceFactory(){
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if(modelClass.isAssignableFrom(UpcomingViewModel::class.java)){
                    return UpcomingViewModel(eventRepository) as T
                }
                throw IllegalArgumentException("")
            }

            companion object{
                @Volatile
                private var instance : UpcomingViewModelFactory? = null

                fun getInstance(context: Context) : UpcomingViewModelFactory =
                    instance ?: synchronized(this){
                        instance ?: UpcomingViewModelFactory(Injection.provideRepository(context))
                    }.also { instance = it }

            }
        }