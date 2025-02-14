package com.org.dicodingeventapp.data.repository

sealed class ResultAdditional <out R> private constructor() {
    data class Success<out T>(val data:T): ResultAdditional<T>()
    data class Error(val error:String) : ResultAdditional<Nothing>()
    object loading : ResultAdditional<Nothing>()
    object isEmpty:  ResultAdditional<Nothing>()
}