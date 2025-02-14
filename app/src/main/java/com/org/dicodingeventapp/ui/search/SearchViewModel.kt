package com.org.dicodingeventapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.data.repository.EventRepository
import com.org.dicodingeventapp.data.repository.Result
import kotlinx.coroutines.launch

class SearchViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty : MutableLiveData<Boolean> = _isEmpty

    private val _listEventItem = MutableLiveData<List<ListEventsItem>>()
    val listEventsItem = _listEventItem

    private val _isErr = MutableLiveData<Boolean>()
    val isErr : MutableLiveData<Boolean> = _isErr

    fun fetchFindEventyQuery(query: String){
        viewModelScope.launch{
            eventRepository.fetchFindEventQuery(query).observeForever { result ->
                when(result){
                    is Result.Error -> {
                        _isErr.value = true
                        _isLoading.value = false
                    }
                    is Result.Success -> {
                        _isLoading.value = false
                        _isErr.value = false
                        _isEmpty.value = false
                        _listEventItem.value = result.data

                    }
                    Result.isEmpty -> {
                        _isEmpty.value = true
                        _isLoading.value = false
                        _isErr.value = false
                    }
                    Result.loading -> {
                        _isLoading.value = true
                    }
                }
            }

        }
    }
}