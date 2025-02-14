package com.org.dicodingeventapp.data.repository

sealed class ResultSearch <out  T> private constructor(){
    data class Success<out T>(val data:T): ResultSearch<T>()
    data class Error(val error:String) : ResultSearch<Nothing>()
    object loading : ResultSearch<Nothing>()
    object isEmpty:  ResultSearch<Nothing>()
}