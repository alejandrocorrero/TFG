package com.correro.alejandro.tfg.data.api.models.econsultresponse

import com.google.gson.annotations.SerializedName
data class Econsult(
    @SerializedName("id") val id: String,
    @SerializedName("id_paciente") val idPaciente: String,
    @SerializedName("id_medico") val idMedico: String,
    @SerializedName("id_especialidad") val idEspecialidad: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("nombre_respuesta") val nombreRespuesta: String,
    @SerializedName("fecharespuesta") val fecharespuesta: String,
    @SerializedName("respuesta") val respuesta: String,
    @SerializedName("leido") val leido: String
)