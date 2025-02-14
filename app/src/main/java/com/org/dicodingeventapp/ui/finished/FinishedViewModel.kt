package com.org.dicodingeventapp.ui.finished

import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.data.repository.EventRepository

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getFinishedEvent() = eventRepository.getFinishedEvent()

}