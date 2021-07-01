package com.airline

import java.util.*

const val PATH_APP = "/airline_war"
const val PORT = 7070
const val HOST = "192.168.1.7"

fun dateToString(date: Date): String {
    return "" + date.year + "-" + td(date.month) + "-" + td(date.date) + "T" + td(date.hours) + ":" + td(date.minutes) + ":" + td(date.seconds) + ".000Z"
    //"2000-04-12T06:00:00.000Z"
}

fun td(n: Int): String {
    return if (n < 10) "0" + n else "" + n
}