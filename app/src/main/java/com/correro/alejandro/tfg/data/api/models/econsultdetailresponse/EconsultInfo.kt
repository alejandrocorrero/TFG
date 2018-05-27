package com.correro.alejandro.tfg.data.api.models.econsultdetailresponse

import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Attachment
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Respuesta
import com.google.gson.annotations.SerializedName
data class EconsultInfo(
        @SerializedName("EConsult") val EConsult: EConsultDetail,
        @SerializedName("Attachments") val attachments: ArrayList<Attachment>,
        @SerializedName("Respuestas") val respuestas: ArrayList<Respuesta>
)