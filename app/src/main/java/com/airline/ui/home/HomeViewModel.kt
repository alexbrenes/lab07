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
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : ViewModel() {

    var archived: ArrayList<Journey>
    var client: HttpClient? = null
    var journeys: MutableLiveData<ArrayList<Journey>> = MutableLiveData()
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)
    var user: User? = null

    init {
        archived = ArrayList()
        journeys.value = ArrayList()
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
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<Journey>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), Journey::class.java))
                journeys.postValue(arrayList)
            }
    }

    fun at(idx: Int): Journey {
        return journeys.value?.get(idx)!!
    }

    companion object {
        private const val PATH_RESERVATION = "$PATH_APP/reservation"
    }

}