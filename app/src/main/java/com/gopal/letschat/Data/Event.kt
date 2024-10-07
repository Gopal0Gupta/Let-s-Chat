package com.gopal.letschat.Data

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