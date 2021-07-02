package com.airline.ui.checkout

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airline.logic.Journey
import com.airline.logic.Paymentmethod
import com.lab04.R
import com.lab04.databinding.ActivityCheckoutBinding
import com.lab04.logic.User

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
        checkoutViewModel.paymentmethods.observe(this) {
            val paymentMethod = findViewById<Spinner>(R.id.paymentMethod)
            val arr = it.toTypedArray()
            var adapterMethods =
                let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item,
                        arr.map { it.paymentmethod })
                }
            paymentMethod?.adapter = adapterMethods
        }
    }

    override fun onPause() {
        super.onPause()
        checkoutViewModel.close()
    }

    override fun onResume() {
        super.onResume()
        checkoutViewModel.open(lifecycleScope)
        checkoutViewModel.openReservation(lifecycleScope)
        val origen_txv = findViewById<TextView>(R.id.origen_txv)
        val destination_txv = findViewById<TextView>(R.id.destination_txv)
        val departure = findViewById<TextView>(R.id.departure_txv)
        val return_txv = findViewById<TextView>(R.id.return_txv)
        val availability_txv = findViewById<TextView>(R.id.availability_txv)
        val price_txv = findViewById<TextView>(R.id.price_txv)
        val NumbSistAmount = findViewById<TextView>(R.id.NumbSistAmount)
        val total_txv = findViewById<TextView>(R.id.total_txv)
        val backdate_txv = findViewById<TextView>(R.id.txvBackdate)
        val bundle = intent.extras
        if (bundle != null) {
            val journey = bundle.get("journey") as Journey
            val user = bundle.get("user") as User
            this.checkoutViewModel.user = user
            this.checkoutViewModel.journey = journey
            origen_txv?.setText(journey.flightIdflight!!.origin!!.name)
            destination_txv?.setText(journey.flightIdflight!!.destination!!.name)
            departure?.setText(journey.date.toString())
            if (journey.isRoundTrip == '1')
                return_txv?.setText(journey.backdate.toString())
            else {
                return_txv?.visibility = View.INVISIBLE
                backdate_txv?.visibility = View.INVISIBLE
            }
            if (journey.isSpecialOffer == '1') {
                price_txv?.setText("" + journey.specialPrice)
                total_txv?.setText("" + journey.specialPrice)
            } else {
                price_txv?.setText("" + journey.price)
                total_txv?.setText("" + journey.price)
            }
            availability_txv?.setText("" + journey.availability)
            NumbSistAmount?.setText("1")
        }

        binding.btnReserve.setOnClickListener {
            val spinner = binding.paymentMethod
            checkoutViewModel.checkout(
                spinner.selectedItem.toString(),
                binding.NumbSistAmount.text.toString().toInt()
            )
            finish()
        }
        binding.btnCancel.setOnClickListener {
            Log.d("RESERVAR", "CANCELAR")
            finish()
        }
        binding.NumbSistAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0 && s?.length!! < 50) {
                    if (checkoutViewModel.journey.isSpecialOffer == '1') {
                        total_txv?.setText(
                            "" + (checkoutViewModel.journey.specialPrice * s.toString().toInt())
                        )
                    } else {
                        total_txv?.setText(
                            "" + (checkoutViewModel.journey.price * s.toString().toInt())
                        )
                    }
                }
            }
        })
    }

}