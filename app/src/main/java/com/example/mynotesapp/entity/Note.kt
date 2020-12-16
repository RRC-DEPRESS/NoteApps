package com.example.mynotesapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var id: Int = 0,
    var NAMA: String? = null,
    var Alamat: String? = null,
    var NIK: String? = null,
    var TempatLahir: String? = null,
    var TanggalLahir: String? = null,
    var date: String? = null
) : Parcelable
