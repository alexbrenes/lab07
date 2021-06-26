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

//        val itemTouchHelperCallback1 = object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//                position = viewHolder.adapterPosition
//                var journey = myReservationViewModel.at(position)
//
//                val bundle = bundleOf(
//                    "journey" to myReservationViewModel.at(position)
//                )
//                //view?.findNavController()?.navigate(R.id.nav_checkoutActivity, bundle)
//                intent.putExtra("reservation", myReservationViewModel.at(position))
//                startActivity(intent)
//                Log.d("swipe", "intent")
//                adapter.notifyItemChanged(position)
//            }
//
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//
//                RecyclerViewSwipeDecorator.Builder(
//                    baseContext,
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//                    .addSwipeLeftBackgroundColor(Color.parseColor("#FF03DAC5"))
//                    .addSwipeRightBackgroundColor(Color.parseColor("#FF03DAC5"))
//                    .create()
//                    .decorate()
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            }
//
//        }
//
//        val itemTouchHelperCallback2 = object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//                position = viewHolder.adapterPosition
//                var journey = myReservationViewModel.at(position)
//
//                val bundle = bundleOf(
//                    "journey" to myReservationViewModel.at(position)
//                )
//                // view?.findNavController()?.navigate(R.id.nav_checkoutActivity, bundle)
//
//                adapter.notifyItemChanged(position)
//            }
//
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//
//                RecyclerViewSwipeDecorator.Builder(
//                    baseContext,
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//                    .addSwipeLeftBackgroundColor(Color.parseColor("#FF03DAC5"))
//                    .addSwipeRightBackgroundColor(Color.parseColor("#FF03DAC5"))
//                    .create()
//                    .decorate()
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            }
//
//        }
//
//        val itemTouchHelper1 = ItemTouchHelper(itemTouchHelperCallback1)
//        itemTouchHelper1.attachToRecyclerView(recyclerView1)
//        val itemTouchHelper2 = ItemTouchHelper(itemTouchHelperCallback2)
//        itemTouchHelper2.attachToRecyclerView(recyclerView2)

    }

    private fun OnInitViewmodel(adapter: AdapterReservation) {
        this.adapter = adapter
        myReservationViewModel.reservations.items.observe(this) { items ->
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