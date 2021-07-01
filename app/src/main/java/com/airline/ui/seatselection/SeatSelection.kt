package com.example.lab_04_app

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.airline.logic.Reservation
import com.airline.ui.seatselection.SeatSelectionViewModel
import com.lab04.databinding.ActivitySeatSelectionBinding
import com.lab04.logic.User

class SeatSelection : AppCompatActivity() {

    private lateinit var binding: ActivitySeatSelectionBinding
    private val seatSelectionViewModel: SeatSelectionViewModel = SeatSelectionViewModel()
    val asientos = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val user = bundle?.get("user") as User
        val reservation = bundle?.get("reservation") as Reservation
        seatSelectionViewModel.user = user
        seatSelectionViewModel.reservation = reservation
        seatSelectionViewModel.reservedSeats.observe(this) {
            cargarAsientos(
                reservation.journeyIdjourney?.flightIdflight?.planeIdplane?.planetypeIdplanetype!!.rows,
                reservation.journeyIdjourney?.flightIdflight?.planeIdplane?.planetypeIdplanetype!!.seatsperrow
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cargarAsientos(filas: Int, columnas: Int) {

        var cant: TextView = binding.tvCantidad
        Log.d("SIZE_TICKETS", "" + seatSelectionViewModel.reservation!!.ticketList!!.size)
        var cantidadA: Int =
            if (seatSelectionViewModel.reservation!!.ticketList!!.size == 0)
                seatSelectionViewModel.reservation!!.seats!!
            else 0
        cant.text = ("" + cantidadA)
        for (j in 1..filas) {
            val layout: LinearLayout = LinearLayout(applicationContext)
            layout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.setHorizontalGravity(Gravity.CENTER)
            layout.orientation = LinearLayout.HORIZONTAL
            for (i in 1..columnas) {
                val btn: Button = Button(applicationContext)
                btn.layoutParams = LinearLayout.LayoutParams(150, 110)
                if (seatSelectionViewModel.isReserved(j.toString() + (i + 64).toChar())) {
                    btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#c499a9"))
                } else {
                    btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed8ab4"))
                }
                btn.tag = (j.toString() + (i + 64).toChar())
                btn.setText(j.toString() + (i + 64).toChar())
                layout.addView(btn)
                btn.setOnClickListener {
                    Log.d("HEX", Integer.toHexString(btn.backgroundTintList!!.defaultColor))
                    Log.d("HEX", Integer.toHexString(14954612))
                    Log.d("IS_RESERVED", (i + 64).toChar() + j.toString())
                    Log.d(
                        "IS_RESERVED",
                        "" + seatSelectionViewModel.isReserved((i + 64).toChar() + j.toString())
                    )

                    if (seatSelectionViewModel.isReserved(j.toString() + (i + 64).toChar())) {
                        btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#c499a9"))
                    } else if (Integer.toHexString(btn.backgroundTintList!!.defaultColor) == "ffe43074") {
                        btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed8ab4"))
                        asientos.remove(btn.tag.toString())
                    } else if (asientos.count() < cantidadA.toInt()) {
                        asientos.add(btn.tag.toString());
                        btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#e43074"))
                    }

                    cant.text = ("" + (cantidadA - asientos.size))
                }
            }
            binding.layoutAsi.addView(layout)
        }
    }


    override fun onResume() {
        super.onResume()
        seatSelectionViewModel.open(lifecycleScope)

        binding.reserveBtn.setOnClickListener {
            seatSelectionViewModel.reserveSeats(asientos)
        }

    }

    override fun onPause() {
        super.onPause()
        seatSelectionViewModel.close()
    }

}