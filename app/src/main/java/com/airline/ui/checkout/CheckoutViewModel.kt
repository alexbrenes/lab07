package com.airline.ui.checkout

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
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutViewModel : ViewModel() {

    var journey: Journey
    var paymentmethods: MutableLiveData<ArrayList<Paymentmethod>> = MutableLiveData()
    var user: User
    var client: HttpClient? = null
    var clientReservation: HttpClient? = null
    val outputEventChannel: Channel<String> = Channel(10)
    val outputEventChannelReservation: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    fun open(coroutineScope: CoroutineScope) {
        client = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_PAYMENTMETHOD,
                host = HOST,
                port = PORT
            ) {
                getPaymentMethods()
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
        }
    }

    fun openReservation(coroutineScope: CoroutineScope) {
        Log.d("OPEN_RESERVATION", "OPEN")
        clientReservation = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            clientReservation!!.webSocket(
                path = PATH_RESERVATION,
                host = HOST,
                port = PORT
            ) {
                Log.d("clientReservation", "DefaultClientWebSocketSession")
                val inputReservation = launch { outputReservation() }
                val outputReservation = launch { input() }
                inputReservation.join()
                outputReservation.join()
            }
            clientReservation?.close()
        }
    }

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
        clientReservation?.close()
        clientReservation = null
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

    private suspend fun DefaultClientWebSocketSession.outputReservation() {
        try {
            Log.d("OUTPUT RESERVATION", "OUTPUUUT")
            outputEventChannelReservation.consumeEach {
                Log.d("OUTPUT RESERVATION", "CONSUME")
                Log.d("onOutput", it)
                send(it)
                Log.d("OUTPUT RESERVATION", "SENT")
            }
        } catch (e: Throwable) {
            Log.e("", "${e.message}", e)
        }
    }

    private fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (properties.containsKey("action"))
            if (properties.getProperty("action") == "getPaymentmethods") {
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<Paymentmethod>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), Paymentmethod::class.java))
                paymentmethods.postValue(arrayList)
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

    private fun getPaymentMethods() {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            properties.setProperty("action", "getPaymentmethods")
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    fun checkout(paymentmethodName: String, seats: Int) {
        viewModelScope.launch {
            var paymentmethod: Paymentmethod? = null
            for (i in 0 until paymentmethods.value!!.size) {
                if (paymentmethodName == paymentmethods.value!!.get(i).paymentmethod) {
                    paymentmethod = paymentmethods.value!!.get(i)
                    break
                }
            }
            val gson = Gson()
            val paymentmethodProp = Properties()
            paymentmethodProp["idpaymentmethod"] = "" + paymentmethod!!.idpaymentmethod

            val userProp = Properties()
            userProp["iduser"] = "" + user.iduser

            val journeyProp = Properties()
            journeyProp["idjourney"] = "" + journey.idjourney
            journeyProp["availability"] = "" + journey.availability

            val reservationProp = Properties()
            reservationProp["journeyIdjourney"] = journeyProp
            reservationProp["seats"] = "" + seats
            reservationProp["userIduser"] = userProp
            reservationProp["paymentmethodIdpaymentmethod"] = paymentmethodProp

            val properties = Properties()
            properties.setProperty("action", "addReservation")
            properties.setProperty("data", gson.toJson(reservationProp))
            Log.d("RESERVATION", gson.toJson(properties))
            outputEventChannelReservation.send(gson.toJson(properties))
            Log.d("RESERVATION_OUTPUT_CHANNEL", "SENT")
        }
    }

    init {
        journey = Journey()
        user = User()
    }

    companion object {
        private const val PATH_PAYMENTMETHOD = "$PATH_APP/paymentmethodmgm"
        private const val PATH_RESERVATION = "$PATH_APP/reservation"
    }

}