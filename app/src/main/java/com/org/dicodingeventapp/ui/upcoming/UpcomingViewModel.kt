package com.org.dicodingeventapp.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.service.data.response.EventResponse
import com.org.dicodingeventapp.service.data.response.ListEventsItem
import com.org.dicodingeventapp.service.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private companion object {
        private const val UPCOMING = 1
        private val TAG = UpcomingFragment::class.java.simpleName

    }

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent : LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError : LiveData<Boolean> = _isError

    private val _isDataEmpty = MutableLiveData(false)
    val isDataEmpty = _isDataEmpty

    init {
        getUpcomingEvent()
    }

    private fun getUpcomingEvent(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(UPCOMING)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if(response.body() != null){
                    if(response.body()?.listEvents?.size == 0){
                        _isDataEmpty.value = true
                    }
                    _isError.value = false
                    _upcomingEvent.value = response.body()?.listEvents
                }else{
                    _isError.value = true
                    Log.d(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.d(TAG, toString())
            }

        })
    }
}