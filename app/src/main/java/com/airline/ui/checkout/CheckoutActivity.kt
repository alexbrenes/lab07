package com.airline.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.lab04.R
import com.lab04.databinding.ActivityCheckoutBinding
import com.lab04.databinding.FragmentCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private var checkoutViewModel: CheckoutViewModel

    init {
        checkoutViewModel = CheckoutViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val price = 470000;
        val origen_txv = findViewById<TextView>(R.id.origen_txv)
        val destination_txv = findViewById<TextView>(R.id.destination_txv)
        val departure = findViewById<TextView>(R.id.departure_txv)
        val return_txv = findViewById<TextView>(R.id.return_txv)
        val availability_txv = findViewById<TextView>(R.id.availability_txv)
        val price_txv = findViewById<TextView>(R.id.price_txv)
        val NumbSistAmount = findViewById<TextView>(R.id.NumbSistAmount)
        val total_txv = findViewById<TextView>(R.id.total_txv)
        var paymentmethods =
            arrayOf("Paypal", "Mastercard", "Visa", "Bitcoin")
        val paymentMethod = findViewById<Spinner>(R.id.paymentMethod)

        var adapterMethods =
            let { ArrayAdapter(it, android.R.layout.simple_spinner_item, paymentmethods) }
        paymentMethod?.adapter = adapterMethods
        origen_txv?.setText("Costa Rica")
        destination_txv?.setText("Suiza")
        departure?.setText("05/12/2021")
        return_txv?.setText("12/12/2021")
        availability_txv?.setText("33")
        price_txv?.setText("" + price)
        NumbSistAmount?.setText("6")
        total_txv?.setText("" + (price * 6))

        binding.btnReserve.setOnClickListener {
            Log.d("RESERVAR","RESERVAR")
            finish()
        }
        binding.btnCancel.setOnClickListener {
            Log.d("RESERVAR","CANCELAR")
            finish()
        }
        binding.NumbSistAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0 && s?.length!! < 50)
                    total_txv?.setText("" + (price * s.toString().toInt()))
            }
        })
    }

}