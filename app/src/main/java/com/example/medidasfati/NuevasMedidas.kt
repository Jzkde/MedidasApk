package com.example.medidasfati

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medidasfati.componentes.SpinnerConf
import com.example.medidasfati.db.MedidasDb
import com.example.medidasfati.models.Presupuesto
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NuevasMedidas : AppCompatActivity() {

    lateinit var base: MedidasDb
    lateinit var sistema: Spinner
    lateinit var ancho: TextInputEditText
    lateinit var alto: TextInputEditText
    lateinit var comando: Spinner
    lateinit var apertura: Spinner
    lateinit var accesorios: TextInputEditText
    lateinit var ambiente: TextInputEditText
    lateinit var observaciones: TextInputEditText
    lateinit var clienteNombre: TextInputEditText
    lateinit var mensaje: TextView
    lateinit var cambios: Button
    lateinit var guardar: Button
    lateinit var eliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevas_medidas)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initControlsUi()

        // Inicialización de los Spinners
        val spinnerSetup = SpinnerConf(this)

        spinnerSetup.setupSistemaSpinner(sistema)
        spinnerSetup.setupComandoSpinner(comando)
        spinnerSetup.setupAperturaSpinner(apertura)

        base = MedidasDb.getDb(applicationContext)

        val presupuesto = intent.getParcelableExtra<Presupuesto>("presupuesto")
        if (presupuesto != null) {
            eliminar.visibility = View.VISIBLE
            cambios.visibility = View.VISIBLE
            guardar.visibility = View.GONE

            sistema.setSelection(getSpinnerPosition(sistema, presupuesto.sistema))
            ancho.setText(presupuesto.ancho.toString())
            alto.setText(presupuesto.alto.toString())
            comando.setSelection(getSpinnerPosition(comando, presupuesto.comando))
            apertura.setSelection(getSpinnerPosition(apertura, presupuesto.apertura))
            accesorios.setText(presupuesto.accesorios)
            ambiente.setText(presupuesto.ambiente)
            observaciones.setText(presupuesto.observaciones)
            clienteNombre.setText(presupuesto.clienteNombre)
        }
    }

    private fun getSpinnerPosition(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }

    fun guardar(v: View) {
        GlobalScope.launch {
            base.presupuesto().nuevo(
                Presupuesto(
                    sistema = sistema.selectedItem.toString(),
                    ancho = ancho.text.toString().toInt(),
                    alto = alto.text.toString().toInt(),
                    comando = comando.selectedItem.toString(),
                    apertura = apertura.selectedItem.toString(),
                    accesorios = accesorios.text.toString(),
                    ambiente = ambiente.text.toString(),
                    observaciones = observaciones.text.toString(),
                    clienteNombre = clienteNombre.text.toString(),
                )
            )
            runOnUiThread {
                mensaje.setTextColor(Color.GREEN)
                mensaje.text = "Medidas GUARDADAS"
                limpiarCampos()
            }
        }
    }

    fun guardarCambios(v: View) {
        GlobalScope.launch {
            val presupuesto = intent.getParcelableExtra<Presupuesto>("presupuesto")
            if (presupuesto != null) {
                // Actualizar los campos con los nuevos valores
                presupuesto.sistema = sistema.selectedItem.toString()
                presupuesto.ancho = ancho.text.toString().toInt()
                presupuesto.alto = alto.text.toString().toInt()
                presupuesto.comando = comando.selectedItem.toString()
                presupuesto.apertura = apertura.selectedItem.toString()
                presupuesto.accesorios = accesorios.text.toString()
                presupuesto.ambiente = ambiente.text.toString()
                presupuesto.observaciones = observaciones.text.toString()
                presupuesto.clienteNombre = clienteNombre.text.toString()

                base.presupuesto().editar(presupuesto)

                runOnUiThread {
                    mensaje.setTextColor(Color.GREEN)
                    mensaje.text = "Cambios GUARDADOS"

                    finish()
                }
            }
        }
    }

    fun eliminar(v: View) {
        val presupuesto = intent.getParcelableExtra<Presupuesto>("presupuesto")
        if (presupuesto != null) {
            GlobalScope.launch {
                base.presupuesto().borrar(presupuesto)
                runOnUiThread {
                    mensaje.setTextColor(Color.RED)
                    mensaje.text = "Medidas ELIMINADAS"
                    finish()
                }
            }
        }
    }

    private fun limpiarCampos() {
        ancho.text?.clear()
        alto.text?.clear()
        accesorios.text?.clear()
        ambiente.text?.clear()
        observaciones.text?.clear()
        clienteNombre.text?.clear()
    }

    private fun initControlsUi() {
        sistema = findViewById(R.id.sistemaSpinner)
        comando = findViewById(R.id.comandoSpinner)
        apertura = findViewById(R.id.aperturaSpinner)
        ancho = findViewById(R.id.ancho)
        alto = findViewById(R.id.alto)
        accesorios = findViewById(R.id.accesorios)
        ambiente = findViewById(R.id.ambiente)
        observaciones = findViewById(R.id.observaciones)
        clienteNombre = findViewById(R.id.clienteNombre)
        mensaje = findViewById(R.id.mensaje)
        guardar = findViewById(R.id.guardar)
        cambios = findViewById(R.id.cambios)
        eliminar = findViewById(R.id.eliminar)

        eliminar.visibility = View.GONE
        cambios.visibility = View.GONE

    }
}
