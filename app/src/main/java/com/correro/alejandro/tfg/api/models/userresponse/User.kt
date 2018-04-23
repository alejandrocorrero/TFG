package com.correro.alejandro.tfg.api.models.userresponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class User(
        @SerializedName("id") val id: String,
        @SerializedName("nombre") val nombre: String,
        @SerializedName("apellido") val apellido: String,
        @SerializedName("email") val email: String,
        @SerializedName("direccion") val direccion: String,
        @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
        @SerializedName("telefono") val telefono: String,
        @SerializedName("movil") val movil: String,
        @SerializedName("pais_nacimiento") val paisNacimiento: String,
        @SerializedName("sexo") val sexo: String,
        @SerializedName("estado_civil") val estadoCivil: String,
        @SerializedName("ocupacion") val ocupacion: String,
        @SerializedName("notas") val notas: String,
        @SerializedName("foto") val foto: String,
        @SerializedName("nombre_medico") val nombreMedico: String,
        @SerializedName("id_medico") val idMedico: String

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
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
        writeString(nombre)
        writeString(apellido)
        writeString(email)
        writeString(direccion)
        writeString(fechaNacimiento)
        writeString(telefono)
        writeString(movil)
        writeString(paisNacimiento)
        writeString(sexo)
        writeString(estadoCivil)
        writeString(ocupacion)
        writeString(notas)
        writeString(foto)
        writeString(nombreMedico)
        writeString(idMedico)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}
