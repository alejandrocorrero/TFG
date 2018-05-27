package com.correro.alejandro.tfg.data.api.models.consultpatientresponse

import com.google.gson.annotations.SerializedName
public data class Respuesta(
    @SerializedName("id") val id: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("respuesta") val respuesta: String,
    @SerializedName("leido") val leido: String,
    @SerializedName("nombre") val nombre: String
)