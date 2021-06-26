package com.lab04

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.airline.ui.planetype.PlanetypeViewModel
import com.lab04.databinding.FragmentGalleryBinding
import com.lab04.databinding.FragmentPlanetypeBinding
import com.lab04.logic.PlaneType

class PlanetypeFragment : Fragment() {

    private var _binding: FragmentPlanetypeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanetypeViewModel

    init{
        viewModel = PlanetypeViewModel()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val addBtn = activity?.findViewById<Button>(R.id.AddPlanetypeBtn)
        val cancelBtn = activity?.findViewById<Button>(R.id.CancelPlanetypeBtn)
        val capacity = activity?.findViewById<EditText>(R.id.capacityPlanetype)
        val manufacturer = activity?.findViewById<EditText>(R.id.manufacturerPlanetype)
        val model = activity?.findViewById<EditText>(R.id.modelPlanetype)
        val rows = activity?.findViewById<EditText>(R.id.rowsPlanetype)
        val seatprow = activity?.findViewById<EditText>(R.id.seatperrowsPlanetype)
        val year = activity?.findViewById<EditText>(R.id.yearPlanetype)
        _binding = FragmentPlanetypeBinding.inflate(inflater, container, false)
        cancelBtn?.setOnClickListener {
            Log.d("CLICK", "CANCELAR")
        }

        addBtn?.setOnClickListener {
            Log.d("CLICK", "ADD")
            viewModel.add(
                year?.text.toString(),
                capacity?.text.toString(),
                seatprow?.text.toString(),
                rows?.text.toString(),
                model?.text.toString(),
                manufacturer?.text.toString())
        }
        return inflater.inflate(R.layout.fragment_planetype, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val case = arguments?.get("case") as Int?
        val planetype: PlaneType? = arguments?.get("planetype") as PlaneType?
        val addBtn = activity?.findViewById<Button>(R.id.AddPlanetypeBtn)
        val cancelBtn = activity?.findViewById<Button>(R.id.CancelPlanetypeBtn)
        val capacity = activity?.findViewById<EditText>(R.id.capacityPlanetype)
        val manufacturer = activity?.findViewById<EditText>(R.id.manufacturerPlanetype)
        val model = activity?.findViewById<EditText>(R.id.modelPlanetype)
        val rows = activity?.findViewById<EditText>(R.id.rowsPlanetype)
        val seatprow = activity?.findViewById<EditText>(R.id.seatperrowsPlanetype)
        val year = activity?.findViewById<EditText>(R.id.yearPlanetype)

        when (case) {
            0 -> { // Select
                if (planetype != null) {
                    addBtn?.visibility = View.INVISIBLE
                    cancelBtn?.visibility = View.INVISIBLE
                    capacity?.setText("" + planetype.capacity)
                    capacity?.isEnabled = false
                    manufacturer?.setText(planetype.manufacturer)
                    manufacturer?.isEnabled = false
                    model?.setText(planetype.model)
                    model?.isEnabled = false
                    rows?.setText("" + planetype.rows)
                    rows?.isEnabled = false
                    seatprow?.setText("" + planetype.seatsperrow)
                    seatprow?.isEnabled = false
                    year?.setText("" + planetype.year)
                    year?.isEnabled = false
                }
            }
            1 -> { // Insert
                addBtn?.visibility = View.VISIBLE
                cancelBtn?.visibility = View.VISIBLE
                capacity?.setText("")
                manufacturer?.setText("")
                model?.setText("")
                rows?.setText("")
                seatprow?.setText("")
                year?.setText("")
            }
            2 -> {
                if (planetype != null) {
                    addBtn?.visibility = View.VISIBLE
                    cancelBtn?.visibility = View.VISIBLE
                    capacity?.setText("" + planetype.capacity)
                    capacity?.isEnabled = true
                    manufacturer?.setText(planetype.manufacturer)
                    manufacturer?.isEnabled = true
                    model?.setText(planetype.model)
                    model?.isEnabled = true
                    rows?.setText("" + planetype.rows)
                    rows?.isEnabled = true
                    seatprow?.setText("" + planetype.seatsperrow)
                    seatprow?.isEnabled = true
                    year?.setText("" + planetype.year)
                    year?.isEnabled = true
                }
            }
        }

    }

}