package com.porfirio.androidprojectpdg.data.remote

import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDetailDto
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PropiedadesApi {
    @GET("propiedades/propiedades_list")
    fun getPropiedadesApiary(): Call<List<PropiedadDto>>


    @GET("propiedades/propiedad_detail/{id}")
    fun getPropiedadDetailApiary(
        @Path("id") id: String?
    ): Call <PropiedadDetailDto>
}