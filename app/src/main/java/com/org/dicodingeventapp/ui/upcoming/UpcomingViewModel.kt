package com.org.dicodingeventapp.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.data.remote.response.EventResponse
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.data.remote.retrofit.ApiConfig
import com.org.dicodingeventapp.data.repository.EventRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
   fun getUpcomingEvent() = eventRepository.getUpcomingEvent()
}