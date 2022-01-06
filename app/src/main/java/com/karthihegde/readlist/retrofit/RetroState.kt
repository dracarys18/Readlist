package com.karthihegde.readlist.retrofit

sealed class RetroState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : RetroState<T>(data = data)
    class Failure<T>(message: String?) : RetroState<T>(message = message)
    class Loading<T> : RetroState<T>()
    class PlaceHolder<T> : RetroState<T>()
}
