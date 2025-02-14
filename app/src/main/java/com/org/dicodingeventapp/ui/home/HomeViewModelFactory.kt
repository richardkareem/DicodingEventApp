package com.org.dicodingeventapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.repository.EventRepository

class HomeViewModelFactory private constructor(private val eventRepository: EventRepository):
        ViewModelProvider.NewInstanceFactory(){

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
                    return HomeViewModel(eventRepository) as T
                }
                        throw IllegalArgumentException("")
            }

            companion object{
                @Volatile
                private var instance : HomeViewModelFactory? = null

                fun getInstance(context: Context) : HomeViewModelFactory=
                    instance ?: synchronized(this){
                        instance ?: HomeViewModelFactory(Injection.provideRepository(context))
                    }.also { instance = it }

            }


}