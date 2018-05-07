package com.correro.alejandro.tfg.data.api.models.medichoraryresponse

import com.google.gson.annotations.SerializedName
data class Horary(
    @SerializedName("dia") val dia: String,
    @SerializedName("hora_inicio") val horaInicio: String,
    @SerializedName("hora_fin") val horaFin: String
)