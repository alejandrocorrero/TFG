package com.correro.alejandro.tfg.data.api.models.consultslistresponse

import com.google.gson.annotations.SerializedName
data class DataConsults(
    @SerializedName("count") val count: Int,
    @SerializedName("rows") val consults: ArrayList<ConsultsList>
)