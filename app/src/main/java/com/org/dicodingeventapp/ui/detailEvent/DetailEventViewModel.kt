
package com.org.dicodingeventapp.ui.detailEvent

import androidx.lifecycle.ViewModel
import com.org.dicodingeventapp.data.local.entity.EventEntity
import com.org.dicodingeventapp.data.remote.response.Event
import com.org.dicodingeventapp.data.repository.EventRepository

class DetailEventViewModel(private var eventRepository: EventRepository) : ViewModel() {

     fun getDetailEvent(id:String) = eventRepository.getDetailEvent(id)

     fun getFavorite(id:String) = eventRepository.getEventDbById(id)


     fun insertFavorite(event: Event) = eventRepository.insertEventToDb(event)
     fun deleteFavoriteEvent(id:String) = eventRepository.deleteEvent(id)
     //
     fun onUnFavorite (eventEntity: EventEntity) = eventRepository.updateEvent(eventEntity, 0)
     fun onFavorite (eventEntity: EventEntity)=
          eventRepository.updateEvent(eventEntity, 1)

     fun getDetailEventById(id:String) = eventRepository.getDetailEventById(id)

}