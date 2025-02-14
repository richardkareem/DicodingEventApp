package com.org.dicodingeventapp.ui.home

sealed class ResultUpcoming <out  T> private constructor(){
    data class Success<out T>(val data:T): ResultUpcoming<T>()
    data class Error(val error:String) : ResultUpcoming<Nothing>()
    object loading : ResultUpcoming<Nothing>()
    object isEmpty:  ResultUpcoming<Nothing>()
}