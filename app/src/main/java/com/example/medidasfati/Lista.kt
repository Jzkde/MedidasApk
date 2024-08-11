package com.example.medidasfati

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medidasfati.adapters.PresupuestoAdapter
import com.example.medidasfati.db.MedidasDb
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Lista : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PresupuestoAdapter
    private lateinit var db: MedidasDb

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

        adapter = PresupuestoAdapter(emptyList()) { presupuesto ->
            val intent = Intent(this, NuevasMedidas::class.java)
            intent.putExtra("presupuesto", presupuesto)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        db = MedidasDb.getDb(applicationContext)

        // Observar los cambios en LiveData
        db.presupuesto().getAll().observe(this, Observer { presupuestos ->
            // Actualizar el adapter con la nueva lista de presupuestos
            adapter.updateData(presupuestos)
        })
    }
    override fun onResume() {
        super.onResume()

        loadPresupuestos()
    }

    private fun loadPresupuestos() {

        db.presupuesto().getAll().observe(this) { presupuestos ->
            adapter.updateData(presupuestos)
        }
    }

}