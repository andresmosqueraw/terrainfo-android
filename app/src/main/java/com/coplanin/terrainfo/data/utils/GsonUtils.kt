package com.coplanin.terrainfo.data.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonUtils {
    private val gson: Gson = GsonBuilder()
        .create()
    
    fun getGson(): Gson = gson
} 