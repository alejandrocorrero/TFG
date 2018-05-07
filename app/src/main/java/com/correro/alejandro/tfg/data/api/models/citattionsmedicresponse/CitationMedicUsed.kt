package com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse

import com.google.gson.annotations.SerializedName
data class CitationMedicUsed(
    @SerializedName("dia") val dia: String,
    @SerializedName("hora") val hora: String
)