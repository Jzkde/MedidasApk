package com.example.medidasfati

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.medidasfati.componentes.PresupuestoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var presViewModel: PresupuestoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializa el ViewModel
        presViewModel = ViewModelProvider(this).get(PresupuestoViewModel::class.java)

        // Configuración de los insets de la ventana
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura botónes para agregar, listar y sincronizar presupuestos
        val btn_nuevo = findViewById<Button>(R.id.btn_nuevo)
        btn_nuevo.setOnClickListener {
            val intent = Intent(this, NuevasMedidas::class.java)
            startActivity(intent)
        }

        val btn_lista = findViewById<Button>(R.id.btn_lista)
        btn_lista.setOnClickListener {
            val intent = Intent(this, Lista::class.java)
            startActivity(intent)
        }

        val btn_sync = findViewById<Button>(R.id.btn_sync)
        btn_sync.setOnClickListener {

            presViewModel.sincronizarConServidor { success, message ->
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        val btn_config = findViewById<Button>(R.id.btn_config)
        btn_config.setOnClickListener {
            val intent = Intent(this, Sync::class.java)
            startActivity(intent)
        }

    }
}
