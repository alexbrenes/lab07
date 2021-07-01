package com.lab04.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airline.*
import com.google.gson.Gson
import com.lab04.logic.User
import com.lab04.persistance.Users
import io.ktor.client.*
import kotlinx.coroutines.channels.Channel
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel : ViewModel() {

    var client: HttpClient? = null
    var user: MutableLiveData<User>
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    init {
        user = MutableLiveData()
    }

    fun open(coroutineScope: CoroutineScope) {
        client = buildClient()
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_LOGIN,
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
        user.postValue(gson.fromJson(properties.getProperty("data"), User::class.java))
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

    private fun buildClient() = HttpClient {
        install(WebSockets)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("email", email)
            req.put("password", password)
            req.put("action", "login")
            outputEventChannel.send(gson.toJson(req))
        }
    }

    companion object {
        private const val PATH_LOGIN = "$PATH_APP/airline"
    }

}