package com.porfirio.androidprojectpdg.data.remote.model

import com.google.gson.annotations.SerializedName

data class PropiedadDto(
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("Propiedad")
    var propiedad: String? = null,
    @SerializedName("Estatus")
    var estatus: String? = null,
    @SerializedName("Tipo")
    var tipo: String? = null,
    @SerializedName("Precio")
    var precio: String? = null
)
