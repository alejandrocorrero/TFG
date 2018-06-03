package com.correro.alejandro.tfg.data.api.models.chronicresponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Chronic(
        @SerializedName("id") val id: String,
        @SerializedName("nombre") val nombre: String,
        @SerializedName("gravedad") val gravedad: String,
        @SerializedName("fecha_deteccion") val fechaDeteccion: String,
        @SerializedName("id_paciente") val idPaciente: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(nombre)
        writeString(gravedad)
        writeString(fechaDeteccion)
        writeString(idPaciente)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Chronic> = object : Parcelable.Creator<Chronic> {
            override fun createFromParcel(source: Parcel): Chronic = Chronic(source)
            override fun newArray(size: Int): Array<Chronic?> = arrayOfNulls(size)
        }
    }
}