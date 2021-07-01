package com.airline.ui.myReservations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.logic.Journey
import com.airline.logic.Paymentmethod
import com.airline.logic.Reservation
import com.airline.persistance.Journeys
import com.airline.persistance.Reservations
import com.airline.ui.checkout.CheckoutViewModel
import com.google.gson.Gson
import com.lab04.logic.User
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class MyReservationViewModel : ViewModel() {

    var oldReservations: MutableLiveData<ArrayList<Reservation>> = MutableLiveData()
    var currentReservations: MutableLiveData<ArrayList<Reservation>> = MutableLiveData()
    var client: HttpClient? = null
    var user: User? = null
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    init {
        oldReservations.value = ArrayList()
        currentReservations.value = ArrayList()
    }

    fun open(coroutineScope: CoroutineScope) {
        client = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_MY_RESERVATION,
                host = HOST,
                port = PORT
            ) {
                getMyReservations()
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
        }
    }

    private fun getMyReservations() {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            properties.setProperty("action", "getMyReservations")
            val userProp = gson.fromJson(gson.toJson(user), Properties::class.java)
            userProp.remove("birthday")
            properties.setProperty("data", gson.toJson(userProp))
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }

    private fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (properties.containsKey("action"))
            when (properties.getProperty("action")) {
                "getMyReservations" -> {
                    Log.d("MY_RESERVATIONS", properties.getProperty("data"))
                    val jsonArray = JSONArray(properties.getProperty("data"))
                    val jsonArrayCurrent = JSONArray(jsonArray.getString(0))
                    val jsonArrayOld = JSONArray(jsonArray.getString(1))
                    val arrayListCurrent = ArrayList<Reservation>()
                    val arrayListOld = ArrayList<Reservation>()
                    for (i in 0 until jsonArrayCurrent.length())
                        arrayListCurrent.add(
                            gson.fromJson(
                                jsonArrayCurrent.getString(i),
                                Reservation::class.java
                            )
                        )
                    for (i in 0 until jsonArrayOld.length())
                        arrayListOld.add(
                            gson.fromJson(
                                jsonArrayOld.getString(i),
                                Reservation::class.java
                            )
                        )
                    Log.d("ARRAYLIST CURRENT", "" + arrayListCurrent.size)
                    Log.d("ARRAYLIST OLD", "" + arrayListOld.size)

                    currentReservations.postValue(arrayListCurrent)
                    oldReservations.postValue(arrayListOld)
                }
                "updateReservationsRes" -> {
                    getMyReservations()
                }
            }
    }

    private suspend fun DefaultClientWebSocketSession.output() {
        try {
            outputEventChannel.consumeEach {
                Log.d("onOutput", it)
                send(it)
            }
        } catch (e: Throwable) {
            Log.e("", "${e.message}", e)
        }
    }

    private suspend fun DefaultClientWebSocketSession.input() {
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                try {
                    Log.d("onMessage", "Message received: ${frame.readText()}")
                    parseRes(frame.readText())

                } catch (e: Throwable) {
                    Log.e("onMessage", "${e.message}", e)
                }
            }
        } catch (e: Throwable) {
            Log.e("onMessage", "${e.message}", e)
        }
    }

    fun at(idx: Int): Reservation {
        return currentReservations.value?.get(idx)!!
    }

    companion object {
        private const val PATH_MY_RESERVATION = "$PATH_APP/myreservations"
    }
}