package com.example.medidasfati

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medidasfati.daos.adapters.MedidaAdapter
import com.example.medidasfati.db.MedidasDb

class Lista : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedidaAdapter
    private lateinit var base: MedidasDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MedidaAdapter(emptyList()) { medida ->
            val intent = Intent(this, NuevasMedidas::class.java)
            intent.putExtra("medida", medida)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        base = MedidasDb.getDb(applicationContext)

        // Observar los cambios en LiveData
        base.medida().getAll().observe(this, Observer { medidas ->
            // Actualizar el adapter con la nueva lista de medidas
            adapter.updateData(medidas)
        })
    }
    override fun onResume() {
        super.onResume()

        loadMedidas()
    }

    private fun loadMedidas() {

        base.medida().getAll().observe(this) { medidas ->
            adapter.updateData(medidas)
        }
    }

}