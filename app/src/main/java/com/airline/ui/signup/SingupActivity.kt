package com.airline.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lab04.R
import com.lab04.databinding.ActivityCheckoutBinding
import com.lab04.databinding.ActivitySingupBinding
import com.lab04.databinding.FragmentReservationBinding

class SingupActivity : AppCompatActivity() {

    private var _binding: ActivitySingupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        _binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.signupSignupBtn.setOnClickListener {
            finish()
        }
        binding.cancelSignupBtn.setOnClickListener {
            finish()
        }
    }

}