package com.airline.ui.summaryReservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.airline.logic.Reservation
import com.lab04.R
import com.lab04.databinding.ActivityNewLoginBinding
import com.lab04.databinding.ActivitySummaryResBinding
import com.lab04.logic.User
import com.lab04.ui.login.LoginViewModel

class SummaryResActivity : AppCompatActivity() {

    private var summaryResViewModel: SummaryResViewModel = SummaryResViewModel()
    private lateinit var binding: ActivitySummaryResBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_res)
        binding = ActivitySummaryResBinding.inflate(layoutInflater)

        val bundle = intent.extras
        val user = bundle?.get("user") as User
        val reservation = bundle?.get("reservation") as Reservation
        summaryResViewModel.user = user
        summaryResViewModel.reservation = reservation
    }

    override fun onResume() {
        super.onResume()

        val fdateDetail = findViewById<TextView>(R.id.fdateDetail)
        val sdateDetail = findViewById<TextView>(R.id.sdateDetail)
        val idreservationDetail = findViewById<TextView>(R.id.idreservationDetail)
        val ordesReservationDetail = findViewById<TextView>(R.id.ordesReservationDetail)
        val priceDetail = findViewById<TextView>(R.id.priceDetail)

        fdateDetail.text =
            summaryResViewModel.reservation!!.journeyIdjourney!!.date.toString()

        sdateDetail.text =
            summaryResViewModel.reservation!!.journeyIdjourney!!.backdate.toString()

        idreservationDetail.text = ("" + summaryResViewModel.reservation!!.idreservation)

        ordesReservationDetail.text =
            (summaryResViewModel.reservation!!.journeyIdjourney!!.flightIdflight!!.origin!!.name + "-" + summaryResViewModel.reservation!!.journeyIdjourney!!.flightIdflight!!.destination!!.name)

        priceDetail.text =
            if (summaryResViewModel.reservation!!.journeyIdjourney!!.isSpecialOffer == '1')
                "" + summaryResViewModel.reservation!!.journeyIdjourney!!.specialPrice
            else
                "" + summaryResViewModel.reservation!!.journeyIdjourney!!.price
        summaryResViewModel.reservation!!.ticketList!!.forEach {
            val tv = TextView(this)
            tv.text = it.idseat
            tv.textSize = 26f
            tv.gravity = Gravity.CENTER
            findViewById<LinearLayout>(R.id.seatsDetailLL).addView(tv)
        }
    }

}