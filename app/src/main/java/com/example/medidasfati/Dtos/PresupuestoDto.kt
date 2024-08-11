package com.example.medidasfati.Dtos

data class PresupuestoDto(
    val sistema: String,
    val ancho: Int,
    val alto: Int,
    val comando: String?,
    val apertura: String?,
    val accesorios: String?,
    val ambiente: String?,
    val observaciones: String?,
    val clienteNombre: String,
 )
