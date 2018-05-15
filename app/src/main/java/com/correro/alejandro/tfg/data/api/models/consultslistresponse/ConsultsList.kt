package com.correro.alejandro.tfg.data.api.models.consultslistresponse

import com.google.gson.annotations.SerializedName
data class ConsultsList(
    @SerializedName("id") val id: String,
    @SerializedName("id_paciente") val idPaciente: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("nombre_medico") val nombreMedico: String,
    @SerializedName("fecharespuesta") val fecharespuesta: String,
    @SerializedName("respuesta") val respuesta: String,
    @SerializedName("leido") val leido: String
)