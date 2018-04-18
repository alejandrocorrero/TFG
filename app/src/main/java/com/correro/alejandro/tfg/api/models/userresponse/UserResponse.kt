package com.correro.alejandro.tfg.api.models.userresponse
import com.google.gson.annotations.SerializedName



data class UserResponse(
		@SerializedName("type") val type: Int,
		@SerializedName("data") val user: User
)



