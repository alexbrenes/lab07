package com.airline.ui.reservation

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.lab04.logic.User
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var reservationViewModel: ReservationViewModel
    private lateinit var adapter: AdapterJourney
    var position: Int = 0

    init {
        reservationViewModel = ReservationViewModel()
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

        val bundle = requireActivity().intent.extras
        val user = bundle?.get("user") as User
        reservationViewModel.user = user

        binding.searchTrip.setOnClickListener {
            reservationViewModel.filter(
                binding.cbxOrCity.selectedItemPosition,
                binding.cbxDesCity.selectedItemPosition,
                binding.fdate.text.toString(),
                binding.sdate.text.toString(),
                binding.numPassengers.text.toString(),
                binding.chkBRoundTrip.isChecked
            )
        }

        binding.cbxOrCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reservationViewModel.getCitiesOr(position)
            }
        }

        binding.cbxDesCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reservationViewModel.getCitiesDes(position)
            }
        }

        ////////////////////////////////////////////////

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                position = viewHolder.adapterPosition
                val bundle = bundleOf(
                    "journey" to reservationViewModel.journeys.value?.get(position),
                    "user" to reservationViewModel.user
                )
                view?.findNavController()?.navigate(R.id.nav_checkoutActivity, bundle)

                adapter.notifyItemChanged(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    activity,
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(Color.parseColor("#FF03DAC5"))
                    .addSwipeRightBackgroundColor(Color.parseColor("#FF03DAC5"))
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        //////////////////////////////////////////////////////////////////////////////////
        return root
    }

    override fun onResume() {
        super.onResume()
        reservationViewModel.open(lifecycleScope)

    }

    override fun onPause() {
        super.onPause()
        reservationViewModel.close()
    }

    private fun OnInitViewmodel(adapter: AdapterJourney) {
        this.adapter = adapter
        reservationViewModel.journeys.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
        reservationViewModel.citiesDes.observe(viewLifecycleOwner) {
            val citiesDes = activity?.findViewById<Spinner>(R.id.cbxDes_City)
            val arr = it.toTypedArray()
            var adapterMethods =
                let {
                    ArrayAdapter(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        arr.map { it.name })
                }
            citiesDes?.adapter = adapterMethods
        }
        reservationViewModel.citiesOr.observe(viewLifecycleOwner) {
            val citiesOr = activity?.findViewById<Spinner>(R.id.cbxOr_City)
            val arr = it.toTypedArray()
            var adapterMethods =
                let {
                    ArrayAdapter(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        arr.map { it.name })
                }
            citiesOr?.adapter = adapterMethods
        }
        reservationViewModel.countries.observe(viewLifecycleOwner) {
            val countriesOr = activity?.findViewById<Spinner>(R.id.cbxOr_Country)
            val countriesDes = activity?.findViewById<Spinner>(R.id.cbxDes_Country)
            val arr = it.toTypedArray()
            var adapterMethods1 =
                let {
                    ArrayAdapter(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        arr.map { it.name })
                }
            var adapterMethods2 =
                let {
                    ArrayAdapter(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        arr.map { it.name })
                }
            countriesOr?.adapter = adapterMethods1
            countriesDes?.adapter = adapterMethods2
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