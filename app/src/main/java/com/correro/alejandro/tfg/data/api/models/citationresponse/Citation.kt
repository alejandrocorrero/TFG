package com.correro.alejandro.tfg.data.api.models.citationresponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Citation(
        @SerializedName("id") val id: String,
        @SerializedName("id_medico") val idMedico: String,
        @SerializedName("id_paciente") val idPaciente: String,
        @SerializedName("dia") val dia: String,
        @SerializedName("hora") val hora: String,
        @SerializedName("nombre_medico") val nombreMedico: String,
        @SerializedName("centro") val centro: String
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
        writeString(idMedico)
        writeString(idPaciente)
        writeString(dia)
        writeString(hora)
        writeString(nombreMedico)
        writeString(centro)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Citation> = object : Parcelable.Creator<Citation> {
            override fun createFromParcel(source: Parcel): Citation = Citation(source)
            override fun newArray(size: Int): Array<Citation?> = arrayOfNulls(size)
        }
    }
}
