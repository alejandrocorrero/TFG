package com.correro.alejandro.tfg.data.api.models.attachmentsresponse

import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val data: Attachment
)