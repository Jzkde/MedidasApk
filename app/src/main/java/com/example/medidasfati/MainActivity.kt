package com.example.medidasfati

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medidasfati.room.db.MedidasDb
import com.example.medidasfati.room.models.Presupuesto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var base: MedidasDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        base = MedidasDb.getDb(applicationContext)

        GlobalScope.launch {
            base.presupuesto().nuevo(
                Presupuesto(
                    sistema = "123",
                    ancho = 120,
                    alto = 1230,
                    comando = "123",
                    apertura = "123",
                    accesorios = "123",
                    ambiente = "123",
                    observaciones = "123",
                    clienteNombre = "123",
                    fecha = "",
                    viejo = false,
                    comprado = false
                )
            )
        }

    }
}