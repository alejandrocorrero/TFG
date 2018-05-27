package com.correro.alejandro.tfg.data.api.models.econsultdetailresponse

import com.google.gson.annotations.SerializedName

data class EconsultDetailResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val econsultInfo: EconsultInfo
)