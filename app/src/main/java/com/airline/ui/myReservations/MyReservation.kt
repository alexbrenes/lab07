package com.airline.ui.myReservations

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_04_app.SeatSelection
import com.lab04.R
import com.lab04.databinding.ActivityMyReservationBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class MyReservation : AppCompatActivity() {

    private var position: Int = 0
    private var myReservationViewModel: MyReservationViewModel
    private var _binding: ActivityMyReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter: AdapterReservation

    init {
        myReservationViewModel = MyReservationViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reservation)

        _binding = ActivityMyReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView1 = binding.rvCurrentJourneys
        recyclerView1.setHasFixedSize(true)
        recyclerView2 = binding.rvPrevJourney
        recyclerView2.setHasFixedSize(true)
        initRecyclerView().let { list ->
            OnInitViewmodel(list[0])
            OnInitViewmodel(list[1])
        }

    }

    private fun OnInitViewmodel(adapter: AdapterReservation) {
        this.adapter = adapter
        myReservationViewModel.currentReservations.observe(this) { items ->
            adapter.items = items
        }
    }

    private fun initRecyclerView(): List<AdapterReservation> {
        val adapter = AdapterReservation()
        binding.rvCurrentJourneys.adapter = adapter
        val llm = LinearLayoutManager(this)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        binding.rvCurrentJourneys.layoutManager = llm
        ////
        val adapter2 = AdapterReservation()
        binding.rvPrevJourney.adapter = adapter
        val llm2 = LinearLayoutManager(this)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        binding.rvPrevJourney.layoutManager = llm2
        return listOf(adapter, adapter2)
    }

}