package com.example.medidasfati

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
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

    // Declaración de variables para la base de datos y los componentes de UI
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
    lateinit var caida: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Permite un diseño de pantalla completa
        setContentView(R.layout.activity_nuevas_medidas)  // Establece el layout de la actividad (Vista)

        // Configuración de los márgenes para los sistemas de ventanas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa los componentes de la UI
        initControlsUi()

        // Configurar CamelCase para clienteNombre
        clienteNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    val camelCaseText = convertToCamelCase(s.toString())
                    // Evita un bucle infinito de cambios
                    if (clienteNombre.text.toString() != camelCaseText) {
                        clienteNombre.setText(camelCaseText)
                        clienteNombre.setSelection(camelCaseText.length) // Mover el cursor al final
                    }
                }
            }
        })

        // Inicialización de los Spinners
        val spinnerSetup = SpinnerConf(this)

        spinnerSetup.setupSistemaSpinner(sistema)
        spinnerSetup.setupComandoSpinner(comando)
        spinnerSetup.setupAperturaSpinner(apertura)

        // Obtiene la instancia de la base de datos
        base = MedidasDb.getDb(applicationContext)

        // Carga un presupuesto existente si se pasa en la Intent
        val presupuesto = intent.getParcelableExtra<Presupuesto>("presupuesto")
        if (presupuesto != null) {
            eliminar.visibility = View.VISIBLE
            cambios.visibility = View.VISIBLE
            guardar.visibility = View.GONE

            // Copmleta los campos con los datos del presupuesto existente
            sistema.setSelection(getSpinnerPosition(sistema, presupuesto.sistema))
            ancho.setText(presupuesto.ancho.toString())
            alto.setText(presupuesto.alto.toString())
            comando.setSelection(getSpinnerPosition(comando, presupuesto.comando))
            apertura.setSelection(getSpinnerPosition(apertura, presupuesto.apertura))
            accesorios.setText(presupuesto.accesorios)
            ambiente.setText(presupuesto.ambiente)
            observaciones.setText(presupuesto.observaciones)
            clienteNombre.setText(presupuesto.clienteNombre)
            caida.isChecked = presupuesto.caida
        }
    }

    // Obtiene la posición de un elemento en el Spinner
    private fun getSpinnerPosition(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }

    // Función para guardar un nuevo presupuesto
    fun guardar(v: View) {
        val clienteSeleccionado = clienteNombre.text.toString()
        val sistemaSeleccionado = sistema.selectedItem.toString()
        val anchoSeleccionado = ancho.text.toString().toIntOrNull()
        val altoSeleccionado = alto.text.toString().toIntOrNull()
        val comandoSeleccionado = comando.selectedItem.toString()
        val aperturaSeleccionada = apertura.selectedItem.toString()

        // Validaciones
        when {
            clienteSeleccionado.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el CLIENTE"
                return
            }
            sistemaSeleccionado.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el tipo de SISTEMA"
                return
            }

            altoSeleccionado == null || altoSeleccionado == 0 -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el ALTO"
                return
            }

            anchoSeleccionado == null || anchoSeleccionado == 0 -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el ANCHO"
                return
            }

            comandoSeleccionado.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el lado del COMANDO"
                return
            }

            aperturaSeleccionada.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el tipo de APERTURA"
                return
            }

            (sistemaSeleccionado == "DUBAI" ||
                    sistemaSeleccionado == "PERSIANA" ||
                    sistemaSeleccionado == "ROLLER" ||
                    sistemaSeleccionado == "ORIENTAL") &&
                    aperturaSeleccionada != "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Este SISTEMA NO tiene APERTURA"
                return
            }

            (sistemaSeleccionado == "DUBAI" ||
                    sistemaSeleccionado == "PERSIANA" ||
                    sistemaSeleccionado == "ROLLER" ||
                    sistemaSeleccionado == "ORIENTAL") &&
                    comandoSeleccionado == "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "A Este SISTEMA le falta el COMANDO"
                return
            }

            sistemaSeleccionado == "TELA" && comandoSeleccionado != "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Este SISTEMA NO tiene COMANDO"
                return
            }

            sistemaSeleccionado == "TELA" && aperturaSeleccionada == "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "A Este SISTEMA le falta la APERTURA"
                return
            }

        }

        // Guarda el nuevo presupuesto en la base de datos
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
                    caida = caida.isChecked
                )
            )
            runOnUiThread {
                mensaje.setTextColor(Color.GREEN)
                mensaje.text = "Medidas GUARDADAS"
                limpiarCampos()
            }
        }
    }

    // Guarda los cambios en el  presupuesto existente
    fun guardarCambios(v: View) {
        val clienteSeleccionado = clienteNombre.text.toString()
        val sistemaSeleccionado = sistema.selectedItem.toString()
        val anchoSeleccionado = ancho.text.toString().toIntOrNull()
        val altoSeleccionado = alto.text.toString().toIntOrNull()
        val comandoSeleccionado = comando.selectedItem.toString()
        val aperturaSeleccionada = apertura.selectedItem.toString()

        // Validaciones
        when {
            clienteSeleccionado.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el CLIENTE"
                return
            }
            sistemaSeleccionado.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el tipo de SISTEMA"
                return
            }

            altoSeleccionado == null || altoSeleccionado == 0 -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el ALTO"
                return
            }

            anchoSeleccionado == null || anchoSeleccionado == 0 -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el ANCHO"
                return
            }

            comandoSeleccionado.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el lado del COMANDO"
                return
            }

            aperturaSeleccionada.isEmpty() -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Falta el tipo de APERTURA"
                return
            }

            (sistemaSeleccionado == "DUBAI" ||
                    sistemaSeleccionado == "PERSIANA" ||
                    sistemaSeleccionado == "ROLLER" ||
                    sistemaSeleccionado == "ORIENTAL") &&
                    aperturaSeleccionada != "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Este SISTEMA NO tiene APERTURA"
                return
            }

            (sistemaSeleccionado == "DUBAI" ||
                    sistemaSeleccionado == "PERSIANA" ||
                    sistemaSeleccionado == "ROLLER" ||
                    sistemaSeleccionado == "ORIENTAL") &&
                    comandoSeleccionado == "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "A Este SISTEMA le falta el COMANDO"
                return
            }

            sistemaSeleccionado == "TELA" && comandoSeleccionado != "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "Este SISTEMA NO tiene COMANDO"
                return
            }

            sistemaSeleccionado == "TELA" && aperturaSeleccionada == "NO_POSEE" -> {
                mensaje.setTextColor(Color.RED)
                mensaje.text = "A Este SISTEMA le falta la APERTURA"
                return
            }

        }

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
                presupuesto.caida = caida.isChecked

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
        caida.isChecked = false

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
        caida   = findViewById(R.id.caida)

        eliminar.visibility = View.GONE
        cambios.visibility = View.GONE

        // Cambiar color del switch al iniciar
        if (caida.isChecked) {
            caida.trackDrawable.setTint(Color.BLACK)
            caida.thumbDrawable.setTint(Color.BLUE)
        } else {
            caida.trackDrawable.setTint(Color.BLUE)
            caida.thumbDrawable.setTint(Color.BLACK)
        }

        // Listener para cambiar color al cambiar estado
        caida.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                caida.trackDrawable.setTint(Color.BLACK) // Cambia a color verde si está activado
                caida.thumbDrawable.setTint(Color.BLUE) // Cambia el color del pulgar también
            } else {
                caida.trackDrawable.setTint(Color.BLUE) // Cambia a color rojo si está desactivado
                caida.thumbDrawable.setTint(Color.BLACK) // Cambia el color del pulgar también
            }
        }
    }

    private fun convertToCamelCase(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    }
}
