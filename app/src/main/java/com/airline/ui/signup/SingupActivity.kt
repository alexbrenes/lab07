package com.airline.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.lab04.MainActivity
import com.lab04.R
import com.lab04.databinding.ActivityCheckoutBinding
import com.lab04.databinding.ActivitySingupBinding
import com.lab04.databinding.FragmentReservationBinding
import com.lab04.logic.User
import java.text.SimpleDateFormat

class SingupActivity : AppCompatActivity() {

    private var _binding: ActivitySingupBinding? = null
    private val binding get() = _binding!!
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        _binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signupViewModel = SignupViewModel()
        signupViewModel.user.observe(this) { user ->
            if (user != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", signupViewModel.user.value)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val email = binding.emailSU
        val password = binding.passwordSU
        val name = binding.nameSU
        val lastname = binding.lastnameSU
        val address = binding.addressSU
        val birthday = binding.birthdaySU
        val phoneWork = binding.phoneWorkSU
        val phonePer = binding.phonePSU

        signupViewModel.open(lifecycleScope)

        binding.signupSignupBtn.setOnClickListener {
            if (
                !email.text.toString().isEmpty()
                && !password.text.toString().isEmpty()
                && !name.text.toString().isEmpty()
                && !lastname.text.toString().isEmpty()
                && !address.text.toString().isEmpty()
                && !birthday.text.toString().isEmpty()
                && !phonePer.text.toString().isEmpty()
                && !phoneWork.text.toString().isEmpty()
            ) {
                val user = User(
                    email.text.toString(),
                    password.text.toString(),
                    name.text.toString(),
                    lastname.text.toString(),
                    birthday.text.toString() + "T06:00:00.000Z",
                    address.text.toString(),
                    phonePer.text.toString(),
                    phoneWork.text.toString()
                )
                signupViewModel.signUp(user)
            } else {
                Toast.makeText(
                    this, "¡Complete todos los campos!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        binding.cancelSignupBtn.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        signupViewModel.close()
    }

}