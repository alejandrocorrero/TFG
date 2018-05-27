package com.correro.alejandro.tfg.data.api.models.econsultdetailresponse

import com.google.gson.annotations.SerializedName
data class EConsultDetail(
    @SerializedName("id") val id: String,
    @SerializedName("id_paciente") val idPaciente: String,
    @SerializedName("id_medico") val idMedico: String,
    @SerializedName("id_especialidad") val idEspecialidad: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("paciente") val paciente: String,
    @SerializedName("medico") val medico: String
)