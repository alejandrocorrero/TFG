package com.correro.alejandro.tfg.data.api.models.attachmentsresponse

import com.google.gson.annotations.SerializedName

data class Attachment(
        @SerializedName("id") val id: Int,
        @SerializedName("fecha") val fecha: String,
        @SerializedName("tamanio") val tamanio: Int,
        @SerializedName("adjunto") val adjunto: String,
        @SerializedName("nombre") val nombre: Int
)