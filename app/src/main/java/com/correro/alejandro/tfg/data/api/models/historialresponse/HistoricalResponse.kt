package com.correro.alejandro.tfg.data.api.models.historialresponse
import com.google.gson.annotations.SerializedName



data class HistoricalResponse(
		@SerializedName("status") val status: Int,
		@SerializedName("message") val message: String,
		@SerializedName("type") val type: Int,
		@SerializedName("data") val dataHistorial: DataHistorial
)
