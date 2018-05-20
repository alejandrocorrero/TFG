package com.correro.alejandro.tfg.data.api.models.consultpatientresponse

import com.google.gson.annotations.SerializedName
data class ConsultInfo(
    @SerializedName("Consult") val consult: Consult,
    @SerializedName("Attachments") val attachments: ArrayList<Attachment>,
    @SerializedName("Respuestas") val respuestas: ArrayList<Respuesta>
)