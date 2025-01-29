package com.org.dicodingeventapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.service.data.response.EventResponse
import com.org.dicodingeventapp.service.data.response.ListEventsItem
import com.org.dicodingeventapp.service.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    companion object{
        private val TAG = HomeFragment::class.java.simpleName
    }

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent  = _upcomingEvent
    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent = _finishedEvent
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading  = _isLoading
    private val _isEmptyUpcoming = MutableLiveData(false)
    val isEmptyUpcoming = _isEmptyUpcoming
    private val _isEmptyFinished = MutableLiveData(false)
    val isEmptyFinished = _isEmptyFinished


    private fun getEvents(status:Int, mutableLiveData : MutableLiveData<List<ListEventsItem>>, isLoadingMutableData: MutableLiveData<Boolean>){
        isLoadingMutableData.value = true
        val client = ApiConfig.getApiService().getEvent(status)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                isLoadingMutableData.value = false
                if(response.body() != null){
                    var data = response.body()?.listEvents
                    if(data != null){
                        if(data.size > 5){
                            data = data.take(5)
                        }
                        if(data.isEmpty()){
                            if(status == 1){
                                _isEmptyUpcoming.value = true
                            }else{
                                _isEmptyFinished.value = true
                            }

                        }
                    }
                    mutableLiveData.value = data
                }else{
                    Log.d(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                isLoadingMutableData.value = false
                Log.d(TAG, toString())
            }

        })

    }
    fun initUpcomingAndFinishedFetch(){
        getEvents(0, _finishedEvent, _isLoading)
        getEvents(1, _upcomingEvent, _isLoading)

    }
}