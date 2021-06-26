package com.lab04.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.airline.ui.signup.SingupActivity
import com.lab04.MainActivity
import com.lab04.databinding.ActivityLoginBinding

import com.lab04.R

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = LoginViewModel()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnLogin = binding.login
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val signupButton = binding.toSignupBtn

        signupButton.setOnClickListener {
            val intent = Intent(this, SingupActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {

            val email = username.text;
            val password = password.text;

            var loginState = loginViewModel.login(email.toString(), password.toString())

            if (loginState) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", loginViewModel.user)
                startActivity(intent)
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        val username = binding.username
        val password = binding.password
        username.setText("")
        password.setText("")
        username.requestFocus()
    }

}