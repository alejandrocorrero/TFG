package com.correro.alejandro.tfg.data.api.models.historialresponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Historical(
        @SerializedName("id") val id: String,
        @SerializedName("causa") val causa: String,
        @SerializedName("notas") val notas: String,
        @SerializedName("fecha") val fecha: String,
        @SerializedName("nombre_medico") val nombreMedico: String,
        @SerializedName("centro_salud") val centroSalud: String,
        @SerializedName("direccion_centro") val direccionCentro: String
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
        writeString(causa)
        writeString(notas)
        writeString(fecha)
        writeString(nombreMedico)
        writeString(centroSalud)
        writeString(direccionCentro)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Historical> = object : Parcelable.Creator<Historical> {
            override fun createFromParcel(source: Parcel): Historical = Historical(source)
            override fun newArray(size: Int): Array<Historical?> = arrayOfNulls(size)
        }
    }
}