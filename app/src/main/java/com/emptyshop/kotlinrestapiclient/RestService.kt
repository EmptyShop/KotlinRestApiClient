package com.emptyshop.kotlinrestapiclient

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

object AppConstantes{
    const val BASE_URL = "http://192.168.1.66:5000"
}

interface RestService {
    @GET("/contact")
    suspend fun obtenerContactos(): Response<ArrayList<Contacto>>

    @GET("/contact/{id}")
    suspend fun obtenerContacto(
        @Path("id") id: Int
    ): Response<Contacto>

    @POST("/contact")
    suspend fun agregarContacto(
        @Body contacto: Contacto
    ): Response<Contacto>

    @PUT("/contact/{id}")
    suspend fun actualizarContacto(
        @Path("id") id: Int,
        @Body contacto: Contacto
    ): Response<APIResponse>

    @DELETE("/contact/{id}")
    suspend fun eliminarContacto(
        @Path("id") id: Int
    ): Response<APIResponse>

    object RetrofitClient{
        val restService: RestService by lazy {
            Retrofit.Builder()
                .baseUrl(AppConstantes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(RestService::class.java)
        }
    }
}