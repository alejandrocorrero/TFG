package com.correro.alejandro.tfg.data.api.models.reciperesponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Recipe(
        @SerializedName("id") val id: String,
        @SerializedName("dosis") val dosis: String,
        @SerializedName("duracion") val duracion: String,
        @SerializedName("tiempo_entre_dosis") val tiempoEntreDosis: String,
        @SerializedName("id_historial") val idHistorial: String,
        @SerializedName("fecha") val fecha: String,
        @SerializedName("nombre") val nombre: String

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(dosis)
        writeString(duracion)
        writeString(tiempoEntreDosis)
        writeString(idHistorial)
        writeString(fecha)
        writeString(nombre)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Recipe> = object : Parcelable.Creator<Recipe> {
            override fun createFromParcel(source: Parcel): Recipe = Recipe(source)
            override fun newArray(size: Int): Array<Recipe?> = arrayOfNulls(size)
        }
    }
}