package com.correro.alejandro.tfg.data.api.models.consultpatientresponse

import com.google.gson.annotations.SerializedName
data class Consult(
    @SerializedName("id") val id: String,
    @SerializedName("id_paciente") val idPaciente: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("nombre") val paciente: String?

)