package com.airline.ui.reservation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airline.logic.Journey
import com.airline.ui.home.AdapterJourney
import com.airline.ui.myReservations.AdapterReservation
import com.airline.ui.myReservations.MyReservationViewModel
import com.lab04.R
import com.lab04.databinding.ActivityMyReservationBinding
import com.lab04.databinding.FragmentHomeBinding
import com.lab04.databinding.FragmentReservationBinding
import com.lab04.logic.PlaneType

class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var reservationViewModel: ReservationViewModel
    private lateinit var adapter: AdapterJourney

    init {
        reservationViewModel = ReservationViewModel()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.rvJourney
        initRecyclerView().let { ad ->
            OnInitViewmodel(ad)
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        val journey: Journey? = arguments?.get("journey") as Journey?

    }

    private fun OnInitViewmodel(adapter: AdapterJourney) {
        this.adapter = adapter
        reservationViewModel.journeys.items.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }

    private fun initRecyclerView(): AdapterJourney {
        val adapter = AdapterJourney()
        binding.rvJourney.adapter = adapter
        val llm = LinearLayoutManager(context)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        binding.rvJourney.layoutManager = llm
        ////
        return adapter
    }

}