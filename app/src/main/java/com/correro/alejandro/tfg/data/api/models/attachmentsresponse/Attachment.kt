package com.correro.alejandro.tfg.data.api.models.attachmentsresponse

import com.google.gson.annotations.SerializedName

data class Attachment(
        @SerializedName("id") val id: String,
        @SerializedName("fecha") val fecha: String,
        @SerializedName("tamanio") private val _tamanio: String,
        @SerializedName("adjunto") val adjunto: String,
        @SerializedName("nombre") val nombre: String


        ) {
    val tamanio: String
        get() =String.format("%.3f MB",_tamanio.toInt() * 0.00000095367432 )

}
