package com.example.medidasfati.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Presupuesto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sistema: String = "",
    val ancho: Int = 0,
    val alto: Int = 0,
    val comando: String = "",
    val apertura: String = "",
    val accesorios: String = "",
    val ambiente: String = "",
    val observaciones: String = "",
    val clienteNombre: String = "",
    val fecha: String = "",
    val viejo: Boolean = false,
    val comprado: Boolean = false
)
