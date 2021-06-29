package com.airline.client

import com.google.gson.Gson
import java.util.*


abstract class Parser {
    fun toReq(code: String, data: String, action: String): String {
        val gson = Gson()
        val req = Properties()
        req.setProperty("action", action)
        req.setProperty("code", code)
        req.setProperty("data", data)
        return gson.toJson(req)
    }

    fun parseRes(message: String): Properties {
        val gson = Gson()
        val properties = gson.fromJson(message, Properties::class.java)
        return properties
    }

}