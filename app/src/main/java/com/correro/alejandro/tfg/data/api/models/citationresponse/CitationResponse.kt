package com.correro.alejandro.tfg.data.api.models.citationresponse
import com.google.gson.annotations.SerializedName



data class CitationResponse(
		@SerializedName("status") val status: Int,
		@SerializedName("message") val message: String,
		@SerializedName("type") val type: Int,
		@SerializedName("data") val citations: ArrayList<Citation>
)

