package com.example.medidasfati.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medida(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var sistema: String = "",
    var ancho: Int = 0,
    var alto: Int = 0,
    var comando: String = "",
    var apertura: String = "",
    var accesorios: String = "",
    var ambiente: String = "",
    var observaciones: String = "",
    var cliente: String = "",
    var caida: Boolean = false ,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),  // Para el id
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)  // Guardar el id
        parcel.writeString(sistema)
        parcel.writeInt(ancho)
        parcel.writeInt(alto)
        parcel.writeString(comando)
        parcel.writeString(apertura)
        parcel.writeString(accesorios)
        parcel.writeString(ambiente)
        parcel.writeString(observaciones)
        parcel.writeString(cliente)
        parcel.writeByte(if (caida) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Medida> {
        override fun createFromParcel(parcel: Parcel): Medida {
            return Medida(parcel)
        }

        override fun newArray(size: Int): Array<Medida?> {
            return arrayOfNulls(size)
        }
    }
}
