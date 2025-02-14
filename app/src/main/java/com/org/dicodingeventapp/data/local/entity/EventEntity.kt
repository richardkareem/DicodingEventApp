package com.org.dicodingeventapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
class EventEntity (
    @field:ColumnInfo("id")
    @field:PrimaryKey(autoGenerate = false)
    val id : Int ,

    @field:ColumnInfo("mediaCover")
	val mediaCover: String,

	@field:ColumnInfo("name")
	val name: String,

	@field:ColumnInfo("cityName")
	val cityName: String,


){
	override fun toString(): String {
		return "event entity: iSFavorite: $name, id: $id media: $mediaCover cityName: $cityName"
	}
}