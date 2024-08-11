package com.example.medidasfati.componentes

import ApiService
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.medidasfati.db.MedidasDb
import com.example.medidasfati.models.Presupuesto
import com.example.medidasfati.Dtos.PresupuestoDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PresupuestoViewModel(application: Application) : AndroidViewModel(application) {

    private val presupuestoDao = MedidasDb.getDb(application).presupuesto()
    private val apiService = RetrofitClient.apiService
    val presupuestos: LiveData<List<Presupuesto>> = presupuestoDao.getAll()

    fun addPresupuesto(newPresupuesto: Presupuesto) {
        viewModelScope.launch(Dispatchers.IO) {
            presupuestoDao.nuevo(newPresupuesto)
        }
    }

    fun sincronizarConServidor(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val urlBase = obtenerUrlSincronizacion()

            // Reconfigura el cliente Retrofit con la URL base obtenida
            val retrofit = Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val presupuestosList = obtenerPresupuestosParaSincronizar()
            Log.d("Sync", "Presupuestos para sincronizar: $presupuestosList")

            if (presupuestosList.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    try {
                        apiService.syncPresupuestos(presupuestosList)
                    } catch (e: Exception) {
                        Log.e("Sync", "Error al sincronizar: ${e.message}", e)
                        null
                    }
                }
                if (response?.isSuccessful == true) {
                    Log.d("Sync", "Sincronización exitosa")
                    eliminarPresupuestosLocales()
                    onResult(true, "Sincronización exitosa")
                } else {
                    Log.e("Sync", "Error en la respuesta del servidor: ${response?.errorBody()?.string()}")
                    onResult(false, "Error en la respuesta del servidor")
                }
            } else {
                onResult(false, "No hay presupuestos para sincronizar")
            }
        }
    }

    private suspend fun obtenerPresupuestosParaSincronizar(): List<PresupuestoDto> {
        return withContext(Dispatchers.IO) {
            val presupuestosList = presupuestoDao.getAllDirect()
            presupuestosList.map { presupuesto ->
                PresupuestoDto(
                    sistema = presupuesto.sistema,
                    ancho = presupuesto.ancho,
                    alto = presupuesto.alto,
                    comando = presupuesto.comando,
                    apertura = presupuesto.apertura,
                    accesorios = presupuesto.accesorios,
                    ambiente = presupuesto.ambiente,
                    observaciones = presupuesto.observaciones,
                    clienteNombre = presupuesto.clienteNombre
                )
            }
        }
    }

    private suspend fun eliminarPresupuestosLocales() {
        withContext(Dispatchers.IO) {
            presupuestoDao.eliminarTodos()
        }
    }

    // Función para obtener la URL de sincronización desde las preferencias compartidas
    private suspend fun obtenerUrlSincronizacion(): String {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = getApplication<Application>().getSharedPreferences("SyncConfig", Context.MODE_PRIVATE)
            val ip = sharedPreferences.getString("IP_ADDRESS", "192.168.0.1")
            val port = sharedPreferences.getString("PORT_NUMBER", "8080")
            "http://$ip:$port/"
        }
    }
}
