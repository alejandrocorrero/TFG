package com.correro.alejandro.tfg.api.models.userresponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("id") val id: Int,
        @SerializedName("username") val username: String,
        @SerializedName("username_canonical") val usernameCanonical: String,
        @SerializedName("email") val email: String,
        @SerializedName("email_canonical") val emailCanonical: String,
        @SerializedName("enabled") val enabled: Boolean,
        @SerializedName("salt") val salt: String,
        @SerializedName("password") val password: String,
        @SerializedName("groups") val groups: List<Any>,
        @SerializedName("roles") val roles: List<String>,
        @SerializedName("nombre") val nombre: String,
        @SerializedName("apellido") val apellido: String,
        @SerializedName("direccion") val direccion: String,
        @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
        @SerializedName("telefono") val telefono: String,
        @SerializedName("movil") val movil: Int,
        @SerializedName("pais") val pais: String,
        @SerializedName("sexo") val sexo: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            ArrayList<Any>().apply { source.readList(this, Any::class.java.classLoader) },
            source.createStringArrayList(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(username)
        writeString(usernameCanonical)
        writeString(email)
        writeString(emailCanonical)
        writeInt((if (enabled) 1 else 0))
        writeString(salt)
        writeString(password)
        writeList(groups)
        writeStringList(roles)
        writeString(nombre)
        writeString(apellido)
        writeString(direccion)
        writeString(fechaNacimiento)
        writeString(telefono)
        writeInt(movil)
        writeString(pais)
        writeString(sexo)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}