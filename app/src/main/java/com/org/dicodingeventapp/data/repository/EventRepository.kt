package com.org.dicodingeventapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.org.dicodingeventapp.data.local.entity.EventEntity
import com.org.dicodingeventapp.data.local.room.EventDao
import com.org.dicodingeventapp.data.remote.response.DetailEventResponse
import com.org.dicodingeventapp.data.remote.response.Event
import com.org.dicodingeventapp.data.remote.response.EventResponse
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.data.remote.retrofit.ApiService
import com.org.dicodingeventapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventsDao : EventDao,
    private val appExecutors: AppExecutors
){

//    private val result = MediatorLiveData<Result<List<EventEntity>>>()
//    private val resultAdditional = MediatorLiveData<ResultAdditional<List<EventEntity>>>()
//    private val resultUpcoming = MediatorLiveData<ResultUpcoming<List<EventEntity>>>()

    fun getUpcomingEvent() : LiveData<Result<List<ListEventsItem>>>{
        val resultUpcoming = MediatorLiveData<Result<List<ListEventsItem>>>()
        resultUpcoming.value = Result.loading
        val client = apiService.getEvent(UPCOMING_EVENT)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        if(data.listEvents.isNotEmpty()){
                            resultUpcoming.value = Result.Success(data.listEvents)
                        }else{
                            resultUpcoming.value = Result.isEmpty
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                resultUpcoming.value = Result.Error("something went wrong ${t.message}")
            }

        })

        return resultUpcoming
    }

    fun getFinishedEvent() : LiveData<Result<List<ListEventsItem>>>{
        val resultUpcoming = MediatorLiveData<Result<List<ListEventsItem>>>()
        resultUpcoming.value = Result.loading
        val client = apiService.getEvent(FINISHED_EVENT)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        if(data.listEvents.isNotEmpty()){
                            resultUpcoming.value = Result.Success(data.listEvents)
                        }else{
                            resultUpcoming.value = Result.isEmpty
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                resultUpcoming.value = Result.Error("something went wrong ${t.message}")
            }

        })

        return resultUpcoming
    }




    fun fetchFindEventQuery(query: String): LiveData<Result<List<ListEventsItem>>>{
        val newResult = MediatorLiveData<Result<List<ListEventsItem>>>()
        newResult.value = Result.loading
        val client = apiService.getSearchEvenet(query)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                val res = response.body()

                if(res != null){
                    if(res.listEvents.isEmpty()){
                        newResult.value = Result.isEmpty
                    }else{
                        newResult.value = Result.Success(res.listEvents)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                newResult.value = Result.Error("Error fetch data ${t.message}")
            }

        })
        return newResult
    }

    fun getAllFavorites() : LiveData<List<EventEntity>>{
        return eventsDao.getFavoritesEvent()
    }

    fun getDetailEvent(id: String) : LiveData<Result<Event>> {
        val resultDetail = MediatorLiveData<Result<Event>>()
        val client = apiService.getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResponse>{
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                resultDetail.value = Result.loading
                if(response.isSuccessful){
                    val res = response.body()
                    val event = res?.event
                    if(event != null){
                        resultDetail.value = Result.Success(event)
                    }else{
                        resultDetail.value = Result.isEmpty
                    }
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                    resultDetail.value = Result.Error("something went wrong ${t.message}")
            }

        })
        return resultDetail
    }

    fun getEventDbById(id:String): LiveData<Boolean>{
        val result = MediatorLiveData<Boolean>()
        val event =  eventsDao.getDetailEvent(id)
        result.addSource(event){ev ->
            result.value = ev != null
        }

        return  result
    }

    fun insertEventToDb(event: Event){
        appExecutors.diskIO.execute {
            val newEvent = EventEntity(
                id = event.id,
                mediaCover = event.mediaCover,
                name = event.name,
                cityName = event.cityName
            )
            eventsDao.insertEvent(newEvent)
        }
    }

    fun deleteEvent(id: String){
        appExecutors.diskIO.execute {
            eventsDao.deleteFavoriteEvent(id)
        }
    }

    fun updateEvent(event: EventEntity, isFavorite: Int){
        appExecutors.diskIO.execute {
            eventsDao.updateEvent(event)
        }
    }

    fun getDetailEventById(id:String): LiveData<EventEntity> {
        return eventsDao.getDetailEvent(id)
    }

    companion object{
        const private val UPCOMING_EVENT = 1
        const private val FINISHED_EVENT = 0
        @Volatile
        private var instance : EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventsDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance?: synchronized(this){
                instance?: EventRepository(apiService, eventsDao, appExecutors)
            }.also { instance = it }

    }

}