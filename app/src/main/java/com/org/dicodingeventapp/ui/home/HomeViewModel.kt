package com.org.dicodingeventapp.ui.home

import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.data.repository.EventRepository

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getUpcomingEvent() = eventRepository.getUpcomingEvent()

    fun getFinishedEvent() = eventRepository.getFinishedEvent()

}