package com.porfirio.androidprojectpdg.data.remote.model

import com.google.gson.annotations.SerializedName

data class PostModelResponse(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("fecha")
    var fecha: String? = null,
    @SerializedName("hora_inicio")
    var inicio: String? = null,
    @SerializedName("hora_fin")  //Asi esta declarada mi variable
    var fin: String? = null,    //Asi la manejo en mi programación
    @SerializedName("asesor")
    var asesor: String? = null,
    @SerializedName("propiedad")
    var propiedadCita: String? = null,
    @SerializedName("cliente")
    var clienteCita: String? = null,
    @SerializedName("mensaje")
    var mensaje: String? = null
)
