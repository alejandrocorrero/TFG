package com.correro.alejandro.tfg.data.api.models.medicusersresponse

import com.google.gson.annotations.SerializedName

data class MedicUserResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val users: ArrayList<MedicUser>
)