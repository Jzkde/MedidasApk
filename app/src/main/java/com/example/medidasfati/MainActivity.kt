package com.example.medidasfati

import PresupuestoApi
import RetrofitClient.retrofit
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medidasfati.db.MedidasDb
import com.example.medidasfati.models.Presupuesto
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var base: MedidasDb
    lateinit var mensa: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        base = MedidasDb.getDb(this)
        mensa = findViewById(R.id.mensa)


        val btn_nuevo = findViewById<Button>(R.id.btn_nuevo)
        btn_nuevo.setOnClickListener {
            val intent = Intent(this, NuevasMedidas::class.java)
            startActivity(intent)
        }

        val btn_lista = findViewById<Button>(R.id.btn_lista)
        btn_lista.setOnClickListener {
            val intent = Intent(this, Lista::class.java)
            startActivity(intent)
        }

        val btn_sync = findViewById<Button>(R.id.btn_sync)
        btn_sync.setOnClickListener {
        }

    }

    fun parcelableToMap(parcelable: Parcelable): Map<String, Any> {
        val parcel = Parcel.obtain()
        parcelable.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val bundle = Bundle().apply {
            readFromParcel(parcel)
        }

        val map = mutableMapOf<String, Any>()
        for (key in bundle.keySet()) {
            map[key] = bundle.get(key)!!
        }

        parcel.recycle()
        return map
    }
}