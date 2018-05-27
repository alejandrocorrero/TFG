package com.correro.alejandro.tfg.data.api.models.econsultresponse

import com.google.gson.annotations.SerializedName

data class EconsultResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val econsults: ArrayList<Econsult>
)