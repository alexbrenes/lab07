package com.lab04.ui.login

import androidx.lifecycle.ViewModel
import com.lab04.logic.User
import com.lab04.persistance.Users

class LoginViewModel : ViewModel(){
    var user: User?
    private var users : Users

    init{
        user = User()
        users = Users.instance
    }

    fun login(email: String, password: String): Boolean {
        user = User(email, password)
        return loginAction()
    }

    private fun loginAction(): Boolean {
        user = users.login(this.user)
        return user !== null
    }
}