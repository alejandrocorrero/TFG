package com.correro.alejandro.tfg.data.api.models.econsultresponse

import com.google.gson.annotations.SerializedName
data class DataEconsults(
    @SerializedName("count") val count: Int,
    @SerializedName("rows") val consults: ArrayList<Econsult>
)