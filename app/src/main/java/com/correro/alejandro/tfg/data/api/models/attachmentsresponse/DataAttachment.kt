package com.correro.alejandro.tfg.data.api.models.attachmentsresponse

import com.google.gson.annotations.SerializedName
data class DataAttachment(
    @SerializedName("count") val count: Int,
    @SerializedName("rows") val rows: ArrayList<Attachment>
)