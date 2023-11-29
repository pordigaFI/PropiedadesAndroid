package com.porfirio.androidprojectpdg.application

import android.app.Application
import com.porfirio.androidprojectpdg.data.PropiedadRepository
import com.porfirio.androidprojectpdg.data.remote.model.RetrofitHelper

class PropiedadesRFApp(): Application() {
    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        PropiedadRepository(retrofit)
    }
}