package com.airline.ui.seatselection

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.logic.*
import com.airline.ui.reservation.ReservationViewModel
import com.google.gson.Gson
import com.lab04.logic.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class SeatSelectionViewModel : ViewModel() {

    var reservedSeats: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
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
                getReservedSeats()
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
        }
    }

    private fun getReservedSeats() {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            val journeyProp = Properties()
            journeyProp["idjourney"] = reservation!!.journeyIdjourney!!.idjourney
            properties["action"] = "getJourneyTickets"
            properties["data"] = gson.toJson(journeyProp)
            Log.d("GETRESERVEDSEATS", gson.toJson(properties))
            outputEventChannel.send(gson.toJson(properties))
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

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }

    fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (!properties.containsKey("action"))
            return
        when (properties.getProperty("action")) {
            "getJourneyTickets" -> {
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<Ticket>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), Ticket::class.java))
                reservedSeats.postValue(arrayList)
            }
        }
    }

    fun isReserved(seatId: String): Boolean {
        reservedSeats.value!!.forEach {
            if (it.idseat == seatId)
                return true
        }
        return false
    }

    fun reserveSeats(asientos: MutableList<String>) {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            val propUser = Properties()
            propUser["idUser"] = user?.iduser
            val jsonArrayTickets = ArrayList<Properties>()
            asientos.forEach {
                val ticket = Properties()
                ticket["idseat"] = it
                jsonArrayTickets.add(ticket)
            }
            val propReservation = Properties()
            propReservation["userIduser"] = propUser
            propReservation["idreservation"] = reservation!!.idreservation
            propReservation["seats"] = reservation!!.seats
            propReservation["ticketList"] = jsonArrayTickets
            properties["action"] = "reserveTickets"
            properties["data"] = gson.toJson(propReservation)
            Log.d("Reserve_seats", gson.toJson(properties))
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    companion object {
        private const val PATH_MY_RESERVATIONS = "$PATH_APP/myreservations"
    }

}