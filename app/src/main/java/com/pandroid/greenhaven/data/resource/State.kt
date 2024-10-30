package com.pandroid.greenhaven.data.resource

sealed class State<out T> {
    data class Idle(val message: String? = null) : State<Nothing>()
    data class Loading(val message: String? = null) : State<Nothing>()
    data class Success<out T>(val data: T) : State<T>()
    data class Error(
        val message: String? = null,
        val exception: Throwable? = null
    ) : State<Nothing>()
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}
