package com.airline.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.logic.Paymentmethod
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

class ProfileViewModel : ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData()
    var client: HttpClient? = null
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    fun open(coroutineScope: CoroutineScope) {
        client = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_PROFILE,
                host = HOST,
                port = PORT
            ) {
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
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

    private fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (properties.containsKey("action")) {
            if (properties.getProperty("action") == "editUser") {
                val userUp = gson.fromJson(properties.getProperty("data"), User::class.java)
                this.user.postValue(userUp)
            }
        }
    }

    fun editUser(newUser: User) {
        viewModelScope.launch {
            val gson = Gson()
            val properties = Properties()

            val userProp = Properties()
            userProp["iduser"] = "" + user.value?.iduser
            userProp["email"] = "" + user.value?.email
            userProp["password"] = "" + newUser.password
            userProp["name"] = "" + newUser.name
            userProp["lastname"] = "" + newUser.lastname
            userProp["birthday"] = "" + newUser.birthday
            userProp["address"] = "" + newUser.address
            userProp["workphone"] = "" + newUser.workphone
            userProp["phone"] = "" + newUser.phone
            userProp["isadmin"] = "0"
            newUser.isadmin = '0'
            newUser.email = user.value!!.email
            newUser.iduser = user.value!!.iduser
            properties.setProperty("action", "editUser")
            properties.setProperty("data", gson.toJson(userProp))
            Log.d("EDIT USER", gson.toJson(properties))
            outputEventChannel.send(gson.toJson(properties))
        }
    }

    private suspend fun DefaultClientWebSocketSession.output() {
        try {
            outputEventChannel.consumeEach {
                send(it)
            }
        } catch (e: Throwable) {
            Log.e("", "${e.message}", e)
        }
    }


    companion object {
        private const val PATH_PROFILE = "$PATH_APP/userprofile"
    }
}