package com.correro.alejandro.tfg.data.api.models.adjuntotest

import com.google.gson.annotations.SerializedName
data class Data(
    @SerializedName("id") val id: Int,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("tam") val tam: Int,
    @SerializedName("path") val path: String
)