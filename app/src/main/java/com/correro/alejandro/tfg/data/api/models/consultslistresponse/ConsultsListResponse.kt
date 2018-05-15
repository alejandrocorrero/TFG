package com.correro.alejandro.tfg.data.api.models.consultslistresponse

import com.google.gson.annotations.SerializedName

data class ConsultsListResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val consults: ArrayList<ConsultsList>
)