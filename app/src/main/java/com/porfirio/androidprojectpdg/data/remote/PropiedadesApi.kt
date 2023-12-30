package com.porfirio.androidprojectpdg.data.remote

import com.porfirio.androidprojectpdg.data.remote.model.PostModelRequest
import com.porfirio.androidprojectpdg.data.remote.model.PostModelResponse
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDetailDto
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PropiedadesApi {
    @GET("propiedades/propiedades_list")
    fun getPropiedadesApiary(): Call<List<PropiedadDto>>

    @GET("propiedades/propiedad_detail/{id}")
    fun getPropiedadDetailApiary(
        @Path("id") id: String?
    ): Call<PropiedadDetailDto>

    @GET("bitacoraCitas/bitacoraCitas_list")
    fun getBitacora(): ArrayList<PostModelResponse>

    @GET("bitacoraCitas/bitacoraCitas_list/{id}")
    fun getBitacoraById(@Path("id") id: String): Response<PostModelResponse>

    @POST("bitacoraCitas/bitacoraCitas_list")
    fun postBitacora(@Body p: PostModelRequest): Response<PostModelResponse>
}