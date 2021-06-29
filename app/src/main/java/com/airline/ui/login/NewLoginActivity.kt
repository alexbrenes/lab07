package com.airline.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.airline.HOST
import com.airline.PATH_APP
import com.airline.PORT
import com.airline.ui.signup.SingupActivity
import com.lab04.MainActivity
import com.lab04.R
import com.lab04.databinding.ActivityLoginBinding
import com.lab04.databinding.ActivityNewLoginBinding
import com.lab04.ui.login.LoginViewModel
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class NewLoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityNewLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_login)

        loginViewModel = LoginViewModel()

        loginViewModel.user.observe(this) { user ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", loginViewModel.user.value)
            startActivity(intent)
        }

        binding = ActivityNewLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val signupButton = binding.toSignupBtn

        signupButton.setOnClickListener {
            val intent = Intent(this, SingupActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {

            val email = username.text
            val password = password.text

            loginViewModel.login(email.toString(), password.toString())

//            if (loginState) {
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("user", loginViewModel.user)
//                startActivity(intent)
//            } else {
//                val toast = Toast.makeText(
//                    applicationContext,
//                    "Credenciales incorrectas",
//                    Toast.LENGTH_SHORT
//                )
//                toast.show()
//            }

        }

    }

    override fun onResume() {
        super.onResume()
        val username = binding.username
        val password = binding.password
        username.setText("")
        password.setText("")
        username.requestFocus()
        loginViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        loginViewModel.close()
    }

}