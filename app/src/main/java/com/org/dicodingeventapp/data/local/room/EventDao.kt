package com.org.dicodingeventapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.org.dicodingeventapp.data.local.entity.EventEntity

@Dao
interface EventDao {


    @Query("SELECT * from event")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM event")
    fun getFavoritesEvent() :  LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getDetailEvent(id: String) : LiveData<EventEntity>

    @Update
    fun updateEvent(event: EventEntity)

    @Query("DELETE FROM event WHERE id = :id")
    fun deleteFavoriteEvent(id: String)

}