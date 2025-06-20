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
import com.example.medidasfati.models.Medida
import com.example.medidasfati.Dtos.MedidaDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MedidaViewModel(application: Application) : AndroidViewModel(application) {

    private val medidaDao = MedidasDb.getDb(application).medida()
    private val apiService = RetrofitClient.apiService
    val medidas: LiveData<List<Medida>> = medidaDao.getAll()

    fun addMedida(newMedida: Medida) {
        viewModelScope.launch(Dispatchers.IO) {
            medidaDao.nuevo(newMedida)
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

            val medidasList = obtenerMedidasParaSincronizar()
            Log.d("Sync", "Medidas para sincronizar: $medidasList")

            if (medidasList.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    try {
                        apiService.syncMedidas(medidasList)
                    } catch (e: Exception) {
                        Log.e("Sync", "Error al sincronizar: ${e.message}", e)
                        null
                    }
                }
                if (response?.isSuccessful == true) {
                    Log.d("Sync", "Sincronización exitosa")
                    eliminarMedidasLocales()
                    onResult(true, "Sincronización exitosa")
                } else {
                    Log.e("Sync", "Error en la respuesta del servidor: ${response?.errorBody()?.string()}")
                    onResult(false, "Error en la respuesta del servidor")
                }
            } else {
                onResult(false, "No hay medidas para sincronizar")
            }
        }
    }

    private suspend fun obtenerMedidasParaSincronizar(): List<MedidaDto> {
        return withContext(Dispatchers.IO) {
            val medidasList = medidaDao.getAllDirect()
            medidasList.map { medida ->
                MedidaDto(
                    sistema = medida.sistema,
                    ancho = medida.ancho,
                    alto = medida.alto,
                    comando = medida.comando,
                    apertura = medida.apertura,
                    accesorios = medida.accesorios,
                    ambiente = medida.ambiente,
                    observaciones = medida.observaciones,
                    cliente = medida.cliente,
                    caida = medida.caida
                )
            }
        }
    }

    private suspend fun eliminarMedidasLocales() {
        withContext(Dispatchers.IO) {
            medidaDao.eliminarTodos()
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
