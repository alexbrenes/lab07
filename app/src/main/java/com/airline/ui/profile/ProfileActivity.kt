package com.airline.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lab04.R
import com.lab04.databinding.ActivityProfileBinding
import com.lab04.databinding.ActivitySingupBinding

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.backProBtn.setOnClickListener {
            finish()
        }
        binding.chnPassProBtn.setOnClickListener {
            val toast = Toast.makeText(
                applicationContext,
                "Contraseña cambiada con éxito",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }
}