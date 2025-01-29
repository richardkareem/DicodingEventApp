package com.org.dicodingeventapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.service.data.response.EventResponse
import com.org.dicodingeventapp.service.data.response.ListEventsItem
import com.org.dicodingeventapp.service.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
//    val isEmpty : MutableLiveData<Boolean> = _isEmpty

    private val _listEventItem = MutableLiveData<List<ListEventsItem>>()
    val listEventsItem = _listEventItem

    private val _isErr = MutableLiveData<Boolean>()
    val isErr : MutableLiveData<Boolean> = _isErr
    fun fetchFindEventyQuery(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchEvenet(query)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                val res = response.body()
                _isLoading.value = false
                if(res != null){
                    if(res.listEvents.isNotEmpty()){
                        _listEventItem.value = res.listEvents
                    }else{
                        _isEmpty.value = true
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _isErr.value = true
            }

        })
    }
}