package com.org.dicodingeventapp.ui.finished

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.repository.EventRepository

class FinishedViewModelFactory private constructor(private val eventRepository: EventRepository):
        //extend view model provider
        ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FinishedViewModel::class.java)){
            return FinishedViewModel(eventRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance : FinishedViewModelFactory? = null

        fun getInstance(context: Context): FinishedViewModelFactory =
            instance?: synchronized(this){
                instance?: FinishedViewModelFactory(Injection.provideRepository(context))
            }.also { instance=it }
    }
        }