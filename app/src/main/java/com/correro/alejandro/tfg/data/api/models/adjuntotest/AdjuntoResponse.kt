package com.correro.alejandro.tfg.data.api.models.adjuntotest

import com.google.gson.annotations.SerializedName

data class AdjuntoResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val data: Data
)