package www.desafio3.marvin

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {

    @GET("recursos/")
    fun obtenerRecursos(): Call<List<Recurso>>

    @GET("recursos/{id}")
    fun obtenerRecursosPorId(@Path("id") id: Int): Call<Recurso>

    @POST("recursos/")
    fun Agregar(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun Actualizar(@Path("id") id: Int, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun Eliminar(@Path("id") id: Int): Call<Void>
}