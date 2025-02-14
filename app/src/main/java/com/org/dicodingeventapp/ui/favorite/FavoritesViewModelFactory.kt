package com.org.dicodingeventapp.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.org.dicodingeventapp.data.di.Injection
import com.org.dicodingeventapp.data.repository.EventRepository

class FavoritesViewModelFactory private constructor(private val eventRepository: EventRepository):
//extend view model provider
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(eventRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance : FavoritesViewModelFactory? = null

        fun getInstance(context: Context): FavoritesViewModelFactory =
            instance?: synchronized(this){
                instance?: FavoritesViewModelFactory(Injection.provideRepository(context))
            }.also { instance=it }
    }
}