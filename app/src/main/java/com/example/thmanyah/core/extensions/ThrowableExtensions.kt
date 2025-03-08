package com.example.thmanyah.core.extensions

import java.io.IOException

fun Throwable.getMappedMessage():String{
    return if (this is IOException)
        "No internet connection"
    else "Something went wrong"
}