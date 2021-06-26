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
import com.lab04.databinding.ActivitySeatSelectionBinding

class SeatSelection : AppCompatActivity() {

    private lateinit var binding: ActivitySeatSelectionBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarAsientos(26,8)
    }

    val asientos = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cargarAsientos(filas: Int, columnas: Int) {

        var cant : TextView = binding.tvCantidad
        //var bundle = intent.extras
        var cantidadA : Int = 5//bundle!!.getInt("cantidadAsientos").toString()

        cant.text = ("" + cantidadA)
        for (i in 1..filas) {
            val layout: LinearLayout = LinearLayout(applicationContext)
            layout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.setHorizontalGravity(Gravity.CENTER)
            layout.orientation = LinearLayout.HORIZONTAL
            for (j in 1..columnas) {
                val btn: Button = Button(applicationContext)
                btn.layoutParams = LinearLayout.LayoutParams(130, 110)
                btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed8ab4"))
                btn.tag = (i + 64).toChar() + j.toString()
                btn.setText((i + 64).toChar() + j.toString())
                layout.addView(btn)
                btn.setOnClickListener {
                    Log.d("HEX", Integer.toHexString(btn.backgroundTintList!!.defaultColor))
                    Log.d("HEX", Integer.toHexString(14954612))
                    if (Integer.toHexString(btn.backgroundTintList!!.defaultColor)  == "ffe43074") {
                        btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ed8ab4"))
                        asientos.remove(btn.tag.toString())
                    } else if(asientos.count() < cantidadA.toInt()) {
                        asientos.add(btn.tag.toString());
                        btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#e43074"))
                    }
                    cant.text = ("" + (cantidadA - asientos.size))
                }
            }
            binding.layoutAsi.addView(layout)
        }
    }


}