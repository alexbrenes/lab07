package com.airline.ui.summaryReservation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.logic.Reservation
import com.airline.logic.Ticket
import com.airline.ui.seatselection.SeatSelectionViewModel
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

class SummaryResViewModel : ViewModel() {

    var user: User? = null
    var client: HttpClient? = null
    var reservation: Reservation? = null
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    fun open(coroutineScope: CoroutineScope) {
        client = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_MY_RESERVATIONS,
                host = HOST,
                port = PORT
            ) {
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
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

    fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (!properties.containsKey("action"))
            return
        when (properties.getProperty("action")) {
            "getJourneyTickets" -> {
            }
        }
    }

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }

    companion object {
        private const val PATH_MY_RESERVATIONS = "$PATH_APP/myreservations"
    }

}