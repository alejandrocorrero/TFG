package com.correro.alejandro.tfg.api.models.userresponse
import com.google.gson.annotations.SerializedName



data class UserResponse(
		@SerializedName("type") val type: Int,
		@SerializedName("status") val status: Int,
		@SerializedName("message") val message: String,
		@SerializedName("data") val user: User

)



