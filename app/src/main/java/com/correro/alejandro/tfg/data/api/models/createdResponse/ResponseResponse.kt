package com.correro.alejandro.tfg.data.api.models.createdResponse

import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Respuesta
import com.google.gson.annotations.SerializedName

data class ResponseResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val respuesta: Respuesta
)