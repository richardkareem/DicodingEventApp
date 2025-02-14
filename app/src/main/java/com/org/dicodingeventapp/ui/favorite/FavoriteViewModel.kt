package com.org.dicodingeventapp.ui.favorite

import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.data.repository.EventRepository

class FavoriteViewModel( private var eventRepository: EventRepository): ViewModel() {

    fun getAllFavorites() = eventRepository.getAllFavorites()
}