package com.correro.alejandro.tfg.data.api.models.specialtiesresponse

import com.google.gson.annotations.SerializedName

data class SpecialtiesResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val specialties: ArrayList<Specialty>
)