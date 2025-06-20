import com.example.medidasfati.Dtos.MedidaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("medidas/sync")
    suspend fun syncMedidas(@Body medidas: List<MedidaDto>): Response<Void>
}
