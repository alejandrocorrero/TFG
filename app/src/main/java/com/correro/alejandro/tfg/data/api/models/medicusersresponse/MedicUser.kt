package com.correro.alejandro.tfg.data.api.models.medicusersresponse

import com.google.gson.annotations.SerializedName
data class MedicUser(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("foto") val foto: String
)