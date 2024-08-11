package com.example.medidasfati.componentes

import android.app.Application
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
}
