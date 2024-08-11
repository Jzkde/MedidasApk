import com.example.medidasfati.Dtos.PresupuestoDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PresupuestoApi {
    @POST("presupuesto/sync")
    suspend fun sincronizarPresupuestos(
        @Body jsonPresupuestos: String
    ): Response<Void>
}