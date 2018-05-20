package com.correro.alejandro.tfg.data.api.models.consultpatientresponse

import com.google.gson.annotations.SerializedName

data class ConsultPatientResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val consultInfo: ConsultInfo
)