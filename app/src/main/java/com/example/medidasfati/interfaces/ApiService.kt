import com.example.medidasfati.Dtos.PresupuestoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("presupuesto/sync")
    suspend fun syncPresupuestos(@Body presupuestos: List<PresupuestoDto>): Response<Void>
}
