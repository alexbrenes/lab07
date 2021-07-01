package com.lab04.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airline.logic.Journey
import com.airline.ui.home.AdapterJourney
import com.google.android.material.snackbar.Snackbar
import com.lab04.R
import com.lab04.databinding.FragmentHomeBinding
import com.lab04.logic.User
import com.lab04.ui.gallery.AdapterPlaneType
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class HomeFragment : Fragment() {

    private var position: Int = 0
    private var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterJourney


    init {
        homeViewModel = HomeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.rvJourneyOffer
        recyclerView.setHasFixedSize(true)

        val bundle = requireActivity().intent.extras
        val user = bundle?.get("user") as User
        homeViewModel.user = user
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
                    "journey" to homeViewModel.at(position),
                    "user" to homeViewModel.user
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
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.open(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView().let { adapter ->
            OnInitViewmodel(adapter)
        }
    }

    private fun OnInitViewmodel(adapter: AdapterJourney) {
        this.adapter = adapter
        homeViewModel.journeys.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }

    private fun initRecyclerView(): AdapterJourney {
        val adapter = AdapterJourney()
        binding.rvJourneyOffer.adapter = adapter
        val llm = LinearLayoutManager(activity)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        binding.rvJourneyOffer.layoutManager = llm
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.close()
    }

}