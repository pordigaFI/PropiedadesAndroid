package com.porfirio.androidprojectpdg.data

import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDetailDto
import com.porfirio.androidprojectpdg.data.remote.PropiedadesApi
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDto
import retrofit2.Call
import retrofit2.Retrofit

class PropiedadRepository(private val retrofit: Retrofit) {
    private val propiedadesApi: PropiedadesApi = retrofit.create(PropiedadesApi :: class.java)

    fun getPropiedadesApiary(): Call<List<PropiedadDto>> =
        propiedadesApi.getPropiedadesApiary()

    fun getPropiedadDetailApiary(id: String?): Call<PropiedadDetailDto> =
        propiedadesApi.getPropiedadDetailApiary(id)
}
