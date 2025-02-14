package com.org.dicodingeventapp.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.repository.EventRepository

class SearchViewModelFactory(private val eventRepository: EventRepository) :

        ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
               return SearchViewModel(eventRepository) as T
       }
        throw IllegalArgumentException("class model unknown")
    }

    companion object{
        @Volatile
        private var instance : SearchViewModelFactory ? = null

        fun getInstance(context: Context) : SearchViewModelFactory =
            instance ?: synchronized(this){
                instance ?: SearchViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }

    }

        }