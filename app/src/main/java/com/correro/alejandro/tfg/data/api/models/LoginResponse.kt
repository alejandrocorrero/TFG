package com.correro.alejandro.tfg.data.api.models
import com.google.gson.annotations.SerializedName



data class LoginResponse(
		@SerializedName("access_token") val accessToken: String,
		@SerializedName("expires_in") val expiresIn: Int,
		@SerializedName("token_type") val tokenType: String,
		@SerializedName("scope") val scope: Any,
		@SerializedName("refresh_token") val refreshToken: String
)