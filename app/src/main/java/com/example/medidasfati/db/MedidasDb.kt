package com.example.medidasfati.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medidasfati.daos.PresupuestoDao
import com.example.medidasfati.models.Presupuesto

@Database(entities = [Presupuesto::class], version = 1)
abstract class MedidasDb : RoomDatabase() {

    abstract fun presupuesto(): PresupuestoDao

    companion object {
        fun getDb(ctx: Context): MedidasDb {
            val db = Room.databaseBuilder(ctx, MedidasDb::class.java, "medidas_fati1").build()
            return db
        }
    }
}