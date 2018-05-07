package com.correro.alejandro.tfg.data.api.models.medichoraryresponse

import com.google.gson.annotations.SerializedName

data class MedicHoraryResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val data: ArrayList<Horary>
)