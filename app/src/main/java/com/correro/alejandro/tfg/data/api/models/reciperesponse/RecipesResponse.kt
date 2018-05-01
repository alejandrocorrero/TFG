package com.correro.alejandro.tfg.data.api.models.reciperesponse
import com.google.gson.annotations.SerializedName



data class RecipesResponse(
		@SerializedName("status") val status: Int,
		@SerializedName("message") val message: String,
		@SerializedName("type") val type: Int,
		@SerializedName("data") val recipes: ArrayList<Recipe>
)

