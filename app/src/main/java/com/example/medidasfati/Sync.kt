package com.example.medidasfati

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.medidasfati.componentes.MedidaViewModel

class Sync : AppCompatActivity() {

    private lateinit var ipAddress: EditText
    private lateinit var portNumber: EditText
    private lateinit var btnGuardar: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        ipAddress = findViewById(R.id.ip_address)
        portNumber = findViewById(R.id.port_number)
        btnGuardar = findViewById(R.id.btn_guardar)

        sharedPreferences = getSharedPreferences("SyncConfig", Context.MODE_PRIVATE)

        // Cargar la configuración guardada
        cargarConfiguracion()

        // Guardar la configuración cuando se haga clic en el botón
        btnGuardar.setOnClickListener {
            guardarConfiguracion()
            finish() // Finaliza la actividad y vuelve a la anterior
        }
    }

    private fun cargarConfiguracion() {
        val ip = sharedPreferences.getString("IP_ADDRESS", "")
        val port = sharedPreferences.getString("PORT_NUMBER", "")

        ipAddress.setText(ip)
        portNumber.setText(port)
    }

    private fun guardarConfiguracion() {
        val editor = sharedPreferences.edit()
        editor.putString("IP_ADDRESS", ipAddress.text.toString())
        editor.putString("PORT_NUMBER", portNumber.text.toString())
        editor.apply()
    }
}