package com.org.dicodingeventapp.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.service.data.response.EventResponse
import com.org.dicodingeventapp.service.data.response.ListEventsItem
import com.org.dicodingeventapp.service.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    companion object{
        private const val NOTACTIVE = 0
    }

    private val _listItem = MutableLiveData<List<ListEventsItem>>()
    val listItem : LiveData<List<ListEventsItem>> = _listItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _isEmpty = MutableLiveData(false)
    val isEmpty = _isEmpty

    private val _isError = MutableLiveData(false)
    val isError = _isError

    init {
        fetchFinishedEvent()
    }

    private  fun fetchFinishedEvent(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(NOTACTIVE)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                val res = response.body()
                if(res != null){
                    if(res.listEvents.isEmpty()){
                        _isEmpty.value = true
                        _isError.value = false
                    }else{
                        _listItem.value = res.listEvents
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }

        })
    }
}