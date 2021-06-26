package com.lab04.ui.gallery

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lab04.R
import com.lab04.databinding.FragmentGalleryBinding
import com.lab04.logic.PlaneType
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

class GalleryFragment : Fragment() {
    private var position: Int = 0

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var adapter: AdapterPlaneType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = GalleryViewModel()

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        recyclerView = binding.rvJobApplication
        recyclerView.setHasFixedSize(true)

        val llm = LinearLayoutManager(activity)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = llm

        binding.addPlaytype.setOnClickListener {
            val bundle = bundleOf(
                "planetype" to PlaneType(),
                "case" to 1
            )
            view?.findNavController()?.navigate(R.id.planetypeFragment, bundle)
        }

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

                if (direction == ItemTouchHelper.LEFT) {
                    var planeType = galleryViewModel.at(position)
                    galleryViewModel.delete(planeType, position)

                    Snackbar.make(
                        recyclerView,
                        "Se eliminó el tipo de avión",
                        Snackbar.LENGTH_LONG
                    ).setAction("Deshacer") {
                        Log.d("UNDO", "RECUPERAR TIPO")
                        galleryViewModel.undo(position)
                    }.show()

                } else {

                    Toast.makeText(context, "" + position + " EDITAR", Toast.LENGTH_SHORT).show()

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
                    .addSwipeLeftBackgroundColor(Color.parseColor("#FF0000"))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(Color.parseColor("#008000"))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
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

        return binding.root

    }

    private fun initRecyclerView(): AdapterPlaneType {
        Log.d("initRecyclerView", "RECYCLER VIEW INICIALIZADO");
        val adapter = AdapterPlaneType()
        binding.rvJobApplication.adapter = adapter
        return adapter
    }

    private fun OnInitViewmodel(adapter: AdapterPlaneType) {
        galleryViewModel.planeTypes.items.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView().let { adapter ->
            OnInitViewmodel(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}