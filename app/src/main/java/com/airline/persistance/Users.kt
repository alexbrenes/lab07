package com.lab04.persistance

import com.lab04.logic.User
import java.util.*
import kotlin.collections.HashMap

class Users {
    private var sequence: Int
    val hashmap: HashMap<String, User>

    init {
        sequence = 0
        hashmap = HashMap()
        loadUsers()
    }

    private object HOLDER {
        val INSTANCE = Users()
    }

    companion object {
        val instance: Users by lazy {
            HOLDER.INSTANCE
        }
    }

    private fun loadUsers() {
        // Común
        val userStd = User(
            ++sequence,
            "std",
            "std",
            "Alex",
            "Brenes",
            Date(),
            "Alajuela",
            "",
            "",
            '0'
        )
        // Admin
        val userAdmin = User(
            ++sequence,
            "admin",
            "admin",
            "Alex",
            "Brenes",
            Date(),
            "Alajuela",
            "",
            "",
            '0'
        )
        hashmap.put(userAdmin.email, userAdmin)
        hashmap.put(userStd.email, userStd)
    }

    fun login(user: User?): User? {
        val userC = hashmap[user?.email]
        if (userC !== null)
            if (userC.password.equals(user?.password))
                return userC
        return null
    }

    fun toList(): List<User> {
        var list: ArrayList<User> = ArrayList()
        for (key in hashmap.keys)
            hashmap[key]?.let { list.add(it) }
        return list
    }

    fun signUp(newUser: User): Boolean {
        newUser.isadmin = '0'
        if (hashmap[newUser.email] != null)
            return false
        hashmap.put(newUser.email, newUser)
        return true
    }

    fun updatePassword(user: User) {
        hashmap[user.email] = user
    }

}