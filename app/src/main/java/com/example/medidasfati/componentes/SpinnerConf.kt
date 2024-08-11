package com.example.medidasfati.componentes

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.medidasfati.enums.Apertura
import com.example.medidasfati.enums.Comando
import com.example.medidasfati.enums.Sistema

class SpinnerConf (private val context: Context) {

    fun setupSistemaSpinner(spinner: Spinner) {
        val sistemas = Sistema.entries.map { it.displaySistema }
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, sistemas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun setupComandoSpinner(spinner: Spinner) {
        val comandos = Comando.entries.map { it.displayComando }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, comandos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun setupAperturaSpinner(spinner: Spinner) {
        val aperturas = Apertura.entries.map { it.displayApertura }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, aperturas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

}