package com.lab04.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.logic.Journey
import com.airline.persistance.Journeys
import com.google.gson.Gson
import com.lab04.logic.PlaneType
import com.lab04.logic.User
import com.lab04.persistance.PlaneTypes
import com.lab04.ui.login.LoginViewModel
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : ViewModel() {

    var archived: ArrayList<Journey>
    var flag = false
    var client: HttpClient? = null
    var journeys: Journeys
    var journeysL: MutableLiveData<ArrayList<Journey>> = MutableLiveData()
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    init {
        archived = ArrayList()
        journeys = Journeys.instance
        journeysL.value = ArrayList()
    }

    fun open(coroutineScope: CoroutineScope) {
        client = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_RESERVATION,
                host = HOST,
                port = PORT
            ) {
                Log.d("TU MAI", "MIS CANCIONES")
                getOfferJourneys()
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
        }
    }

    fun getOfferJourneys() {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            properties.setProperty("action", "getSpecialJourneys")
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
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

    private fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (properties.containsKey("action"))
            if (properties.getProperty("action") == "getSpecialJourneys") {
                Log.d("SPECIAL JOURNEYS", "BEFORE")
                val specialJourneysJSON =
                    gson.fromJson(
                        properties.getProperty("data"),
                        ArrayList::class.java
                    ) as ArrayList<String>
                val specialJourneys = ArrayList<Journey>()
                for (journey in specialJourneysJSON) {
                    specialJourneys.add(gson.fromJson(journey, Journey::class.java))
                }
                Log.d("SPECIAL JOURNEYS", "AFTER " + specialJourneysJSON[0])
            }
        //user.postValue(gson.fromJson(properties.getProperty("data"), User::class.java))
    }

//    fun delete(journey: Journey, position: Int) {
//        archived.add(journey)
//        journeys.delete(position)
//    }


    fun undo(position: Int) {
        if (archived.size <= 0)
            return
        journeys.add(archived.get(archived.size - 1), position)
    }

    fun at(idx: Int): Journey {
        return journeys.items.value?.get(idx)!!
    }

    companion object {
        private const val PATH_RESERVATION = "$PATH_APP/reservation"
    }

}