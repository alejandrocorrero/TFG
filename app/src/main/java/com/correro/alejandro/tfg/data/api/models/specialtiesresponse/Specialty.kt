package com.correro.alejandro.tfg.data.api.models.specialtiesresponse

import com.google.gson.annotations.SerializedName
data class Specialty(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)