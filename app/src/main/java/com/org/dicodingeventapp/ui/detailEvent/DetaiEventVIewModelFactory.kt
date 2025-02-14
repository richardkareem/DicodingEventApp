package com.org.dicodingeventapp.ui.detailEvent

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.repository.EventRepository
import com.org.dicodingeventapp.ui.finished.FinishedViewModel
import com.org.dicodingeventapp.ui.finished.FinishedViewModelFactory

class DetaiEventVIewModelFactory private constructor(private val eventRepository: EventRepository):
//extend view model provider
ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailEventViewModel::class.java)){
            return DetailEventViewModel(eventRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance : DetaiEventVIewModelFactory? = null

        fun getInstance(context: Context): DetaiEventVIewModelFactory =
            instance?: synchronized(this){
                instance?: DetaiEventVIewModelFactory(Injection.provideRepository(context))
            }.also { instance=it }
    }
}