package com.org.dicodingeventapp.data.repository

sealed class Result <out  T> private constructor(){
    data class Success<out T>(val data:T): Result<T>()
    data class Error(val error:String) : Result<Nothing>()
    object loading : Result<Nothing>()
    object isEmpty:  Result<Nothing>()
}