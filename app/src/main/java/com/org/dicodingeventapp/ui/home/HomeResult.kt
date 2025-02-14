package com.org.dicodingeventapp.ui.home

sealed class HomeResult <out  T> private constructor(){
    data class SuccessUpcoming<out T>(val data:T): HomeResult<T>()
    data class SuccessFinished<out T>(val data:T): HomeResult<T>()
    data class Error(val error:String) : HomeResult<Nothing>()
    object loading : HomeResult<Nothing>()
    object isEmptyUpcoming:  HomeResult<Nothing>()
    object isEmptyFinished: HomeResult<Nothing>()
}