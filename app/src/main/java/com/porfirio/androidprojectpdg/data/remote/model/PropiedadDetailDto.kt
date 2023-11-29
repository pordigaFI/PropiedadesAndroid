package com.porfirio.androidprojectpdg.data.remote.model

import com.google.gson.annotations.SerializedName


data class PropiedadDetailDto(
    @SerializedName("imagen")
    var imagen: String? = null,
    @SerializedName("Calle")
    var calle: String? = null,
    @SerializedName("Colonia")  //Asi esta declarada mi variable
    var colonia: String? = null,   //Asi la manejo en mi programación
    @SerializedName("Municipio")
    var municipio: String? = null,
    @SerializedName("Estado")
    var estado: String? = null,
    @SerializedName("m2_Terreno")
    var m2_terreno: String? = null,
    @SerializedName("m2_Construcción")
    var m2_construccion: String? = null,
    @SerializedName("Descripción")
    var descripcion: String? = null,
    @SerializedName("Latitud")
    var lat: Double? = null,
    @SerializedName("Longitud")
    var lon: Double? = null
)

