package com.example.medidasfati.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medidasfati.daos.MedidaDao
import com.example.medidasfati.models.Medida

@Database(entities = [Medida::class], version = 1)
abstract class MedidasDb : RoomDatabase() {

    abstract fun medida(): MedidaDao

    companion object {
        fun getDb(ctx: Context): MedidasDb {
            val db = Room.databaseBuilder(ctx, MedidasDb::class.java, "medidasfati").build()
            return db
        }
    }
}