package com.correro.alejandro.tfg.data.api.models.chronicresponse
import com.google.gson.annotations.SerializedName



data class ChronicResponse(
		@SerializedName("status") val status: Int,
		@SerializedName("message") val message: String,
		@SerializedName("type") val type: Int,
		@SerializedName("data") val chronics: ArrayList<Chronic>
)

