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
import com.airline.logic.Ticket
import com.airline.ui.seatselection.SeatSelectionViewModel
import com.lab04.databinding.ActivitySeatSelectionBinding
import com.lab04.logic.User
import kotlinx.coroutines.channels.ticker
import kotlin.math.asin

class SeatSelection : AppCompatActivity() {

    private lateinit var binding: ActivitySeatSelectionBinding
    private val seatSelectionViewModel: SeatSelectionViewModel = SeatSelectionViewModel()
    val asientos = mutableListOf<String>()
    val reservedSeats = ArrayList<String>()

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

        ////
        seatSelectionViewModel.outReserved.observe(this) {
            val filas =
                seatSelectionViewModel.reservation!!.journeyIdjourney?.flightIdflight?.planeIdplane?.planetypeIdplanetype!!.rows
            reservedSeats.forEach {
                if (isRes(asientos.toList(), it)){
                    val j = if (it.length > 2)
                        (it[0] + "" + it[1]).toInt()
                    else
                        it[0].toInt() - 48
                    val i = it[it.length - 1].toInt() - 64
                    val btn =
                        findViewById<Button>(j * filas + i)
                    btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed8ab4"))
                }
            }
            it.forEach {
                val j = if (it.idseat.length > 2)
                    (it.idseat[0] + "" + it.idseat[1]).toInt()
                else
                    it.idseat[0].toInt() - 48
                val i = it.idseat[it.idseat.length - 1].toInt() - 64
                val btn =
                    findViewById<Button>(j * filas + i)
                if (asientos.indexOf(it.idseat) == -1) {
                    reservedSeats.add(it.idseat)
                    btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#c499a9"))
                }
            }
        }
        ////

    }

    private fun isRes(arr: List<String>, ticket: String): Boolean {
        arr.forEach {
            if (it == ticket)
                return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cargarAsientos(filas: Int, columnas: Int) {

        var cant: TextView = binding.tvCantidad
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
                btn.id = j * filas + i
                layout.addView(btn)
                btn.setOnClickListener {

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
        seatSelectionViewModel.openSeats(lifecycleScope)

        binding.reserveBtn.setOnClickListener {
            seatSelectionViewModel.reserveSeats(asientos)
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        seatSelectionViewModel.close()
    }

}