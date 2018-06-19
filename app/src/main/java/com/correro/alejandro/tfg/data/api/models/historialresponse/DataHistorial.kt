package com.correro.alejandro.tfg.data.api.models.historialresponse
import com.google.gson.annotations.SerializedName

data class DataHistorial(
    @SerializedName("count") val count: Int,
    @SerializedName("rows") val historicals: ArrayList<Historical>
)