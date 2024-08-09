package com.example.medidasfati.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medidasfati.room.models.Presupuesto


@Dao
interface PresupuestoDao {
    @Insert
    suspend fun nuevo(presupuesto: Presupuesto)

    @Update
    suspend fun editar(presupuesto: Presupuesto)

    @Delete
    suspend fun borrar(presupuesto: Presupuesto)

    @Query("SELECT * FROM presupuesto")
    fun getAll ():LiveData< List<Presupuesto>>

    @Query("SELECT * FROM presupuesto WHERE id = :id")
    fun getById(id: Long):LiveData< Presupuesto>

}