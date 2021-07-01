package com.airline.ui.myReservations

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab04.R
import com.lab04.databinding.ActivityMyReservationBinding
import com.lab04.logic.User
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class MyReservationsFragment : Fragment() {

    private var position: Int = 0
    private var myReservationViewModel: MyReservationViewModel
    private var _binding: ActivityMyReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapterCurrent: AdapterReservation
    private lateinit var adapterOld: AdapterReservation

    init {
        myReservationViewModel = MyReservationViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ActivityMyReservationBinding.inflate(layoutInflater)
        recyclerView1 = binding.rvCurrentJourneys
        recyclerView1.setHasFixedSize(true)
        recyclerView2 = binding.rvPrevJourney
        recyclerView2.setHasFixedSize(true)

        val bundle = requireActivity().intent.extras
        val user = bundle?.get("user") as User
        myReservationViewModel.user = user


        ////////////////////////////// CURRENT
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

                if (direction == ItemTouchHelper.RIGHT) {
                    position = viewHolder.adapterPosition
                    val bundle = bundleOf(
                        "reservation" to myReservationViewModel.currentReservations.value?.get(
                            position
                        ),
                        "user" to myReservationViewModel.user
                    )
                    view?.findNavController()?.navigate(R.id.seatSelection, bundle)

                    adapterCurrent.notifyItemChanged(position)
                } else if (direction == ItemTouchHelper.LEFT) {
                    position = viewHolder.adapterPosition
                    val bundle = bundleOf(
                        "reservation" to myReservationViewModel.currentReservations.value?.get(
                            position
                        ),
                        "user" to myReservationViewModel.user
                    )
                    view?.findNavController()?.navigate(R.id.summaryResActivity, bundle)

                    adapterCurrent.notifyItemChanged(position)
                }

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
                    .addSwipeLeftBackgroundColor(Color.parseColor("#ed8ab4"))
                    .addSwipeRightBackgroundColor(Color.parseColor("#ed8ab4"))
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
        itemTouchHelper.attachToRecyclerView(recyclerView1)
        ////////////////////////////// CURRENT


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView().let { list ->
            adapterCurrent = list[0]
            adapterOld = list[1]
            myReservationViewModel.currentReservations.observe(viewLifecycleOwner) { items ->
                Log.d("MY_RESERVATION current", "change")
                Log.d("MY_RESERVATION current", "" + items.size)

                adapterCurrent.items = items
            }
            myReservationViewModel.oldReservations.observe(viewLifecycleOwner) { items ->
                Log.d("MY_RESERVATION old", "change")
                Log.d("MY_RESERVATION old", "" + items.size)
                adapterOld.items = items
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myReservationViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        myReservationViewModel.close()
    }

    private fun initRecyclerView(): List<AdapterReservation> {
        val adapter = AdapterReservation()
        binding.rvCurrentJourneys.adapter = adapter
        val llm = LinearLayoutManager(context)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        binding.rvCurrentJourneys.layoutManager = llm
        ////
        val adapter2 = AdapterReservation()
        binding.rvPrevJourney.adapter = adapter2
        val llm2 = LinearLayoutManager(context)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        binding.rvPrevJourney.layoutManager = llm2
        return listOf(adapter, adapter2)
    }
}