
package com.org.dicodingeventapp.ui.detailEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.service.data.response.DetailEventResponse
import com.org.dicodingeventapp.service.data.response.Event
import com.org.dicodingeventapp.service.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private companion object {
        private val TAG = DetailEventActivity::class.java.simpleName
    }

    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent : LiveData<Event> = _detailEvent

    private val _isLoading =  MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError
     fun getDetailEvent(id:String){
         _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResponse>{
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                val res = response.body()
                if(res != null){
                    _detailEvent.value = res.event

                }else{
                    Log.d(TAG, "on fialed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }
}