package com.correro.alejandro.tfg.data.api.models.consultpatientresponse

import com.google.gson.annotations.SerializedName
data class Attachment(
    @SerializedName("id") val id: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("tamanio") val tamanio: String,
    @SerializedName("adjunto") val adjunto: String,
    @SerializedName("nombre") val nombre: String
)