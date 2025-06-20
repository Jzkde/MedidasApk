package com.example.medidasfati.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medidasfati.models.Medida


@Dao
interface MedidaDao {
    @Insert
    suspend fun nuevo(medida: Medida)

    @Update
    suspend fun editar(medida: Medida)

    @Delete
    suspend fun borrar(medida: Medida)

    @Query("SELECT * FROM medida")
    fun getAll(): LiveData<List<Medida>>

    @Query("SELECT * FROM medida WHERE id = :id")
    fun getById(id: Long): LiveData<Medida>

    @Query("DELETE FROM medida")
    suspend fun eliminarTodos()

    @Query("SELECT * FROM medida")
    suspend fun getAllDirect(): List<Medida>

}