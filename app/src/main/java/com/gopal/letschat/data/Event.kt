package com.gopal.letschat.data

class Event<out T>(val content : T) {
    var hasHandled = false
    fun getContentorNull():T?{
        return if (hasHandled) null
        else{
            hasHandled = true
            content
        }
    }
}