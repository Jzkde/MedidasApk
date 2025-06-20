package com.example.medidasfati.Dtos

data class MedidaDto(
    val sistema: String,
    val ancho: Int,
    val alto: Int,
    val comando: String?,
    val apertura: String?,
    val accesorios: String?,
    val ambiente: String?,
    val observaciones: String?,
    val cliente: String,
    val caida: Boolean
 )
