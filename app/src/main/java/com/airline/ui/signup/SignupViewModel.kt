package com.airline.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.*
import com.google.gson.Gson
import com.lab04.logic.User
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*

class SignupViewModel : ViewModel() {

    var client: HttpClient? = null
    var user: MutableLiveData<User> = MutableLiveData()
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    init {
        user.value = null
    }

    fun open(coroutineScope: CoroutineScope) {
        client = HttpClient {
            install(WebSockets)
        }
        coroutineScope.launch {
            Log.d("SignUpWebSocket", "LAUNCH")
            client!!.webSocket(
                path = PATH_SIGNUP,
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
        Log.d("OUTPUT_SIGNUP", "output")
        try {
            outputEventChannel.consumeEach {
                Log.d("SEND_signUp", it)
                send(it)
            }
        } catch (e: Throwable) {
            Log.e("", "${e.message}", e)
        }
    }

    private fun parseRes(res: String) {
        Log.d("RESPONSE_SIGNUP", res)
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)
        if (properties.containsKey("action")) {
            if (properties.getProperty("action") == "login")
                user.postValue(gson.fromJson(properties.getProperty("data"), User::class.java))
        } else {
            user.postValue(null)
        }
    }


    fun signUp(user: User) {
        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("data", gson.toJson(user))
            req.put("action", "addUser")
            outputEventChannel.send(gson.toJson(req))
        }
    }

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }

    companion object {
        private const val PATH_SIGNUP = "$PATH_APP/signUp"
    }

}