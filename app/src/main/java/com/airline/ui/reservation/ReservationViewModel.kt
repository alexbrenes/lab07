package com.airline.ui.reservation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.logic.City
import com.airline.logic.Country
import com.airline.logic.Journey
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

class ReservationViewModel : ViewModel() {

    var journeys: MutableLiveData<ArrayList<Journey>> = MutableLiveData()
    var journey: Journey
    var client: HttpClient? = null
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)
    var countries: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    var citiesDes: MutableLiveData<ArrayList<City>> = MutableLiveData()
    var citiesOr: MutableLiveData<ArrayList<City>> = MutableLiveData()
    var user: User? = null


    init {
        journey = Journey()
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
                getJourneys()
                getCountries()
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
        }
    }

    private fun getJourneys() {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            properties.setProperty("action", "getJourneys")
            val filterProp = Properties()
            filterProp["destination"] = "0"
            filterProp["fdate"] = ""
            filterProp["is_round_trip"] = "false"
            filterProp["numPassengers"] = ""
            filterProp["origin"] = "0"
            filterProp["sdate"] = ""
            properties["filter"] = gson.toJson(filterProp)
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    fun getCitiesDes(position: Int) {
        Log.d("GET_CITIES", "DESTINATION")
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            val countryProp = Properties()
            countryProp["idcountry"] = countries.value!!.get(position).idcountry
            properties["action"] = "getCities"
            properties["for"] = "Destination"
            properties["data"] = gson.toJson(countryProp)
            Log.d("GET_CITIES", gson.toJson(properties))
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    fun getCitiesOr(position: Int) {
        Log.d("GET_CITIES", "ORIGIN")
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            val countryProp = Properties()
            countryProp["idcountry"] = countries.value!!.get(position).idcountry
            properties["action"] = "getCities"
            properties["for"] = "Origin"
            properties["data"] = gson.toJson(countryProp)
            Log.d("GET_CITIES", gson.toJson(properties))
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    fun getCountries() {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()
            properties.setProperty("action", "getCountries")
            Log.d("GET_COUNTRIES", gson.toJson(properties))
            outputEventChannel.send(gson.toJson(properties))
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

    private fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        Log.d("parseRes", res)
        if (!properties.containsKey("action"))
            return
        when (properties.getProperty("action")) {
            "getJourneysRes" -> {
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<Journey>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), Journey::class.java))
                journeys.postValue(arrayList)
            }
            "getCountriesRes" -> {
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<Country>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), Country::class.java))
                countries.postValue(arrayList)
            }
            "getCitiesByCountryDestination" -> {
                Log.d("parseRes", "getCitiesByCountryDestination")
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<City>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), City::class.java))
                citiesDes.postValue(arrayList)
            }
            "getCitiesByCountryOrigin" -> {
                Log.d("parseRes", "getCitiesByCountryOrigin")
                val jsonArray = JSONArray(properties.getProperty("data"))
                val arrayList = ArrayList<City>()
                for (i in 0 until jsonArray.length())
                    arrayList.add(gson.fromJson(jsonArray.getString(i), City::class.java))
                Log.d("arrListOrigin", "" + arrayList.size)
                citiesOr.postValue(arrayList)
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

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }

    fun filter(
        originPos: Int,
        destinationPos: Int,
        fdate: String,
        sdate: String,
        numPassanger: String,
        isRoundTrip: Boolean
    ) {
        if (fdate.isEmpty()){
            getJourneys()
            return
        }
        viewModelScope.launch {
            val gson = Gson()
            val filterProp = Properties()
            filterProp["origin"] = citiesOr.value!!.get(originPos).idcity
            filterProp["destination"] = citiesDes.value!!.get(destinationPos).idcity
            filterProp["fdate"] = fdate
            filterProp["sdate"] = sdate
            filterProp["numPassengers"] = numPassanger
            filterProp["is_round_trip"] = isRoundTrip
            val properties = Properties()
            properties["action"] = "getJourneys"
            properties["filter"] = gson.toJson(filterProp)
            Log.d("FILTER", gson.toJson(filterProp))
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    companion object {
        private const val PATH_RESERVATION = "$PATH_APP/reservation"
    }

}