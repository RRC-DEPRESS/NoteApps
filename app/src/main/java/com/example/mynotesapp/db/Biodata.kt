package com.example.mynotesapp.db

import android.provider.BaseColumns

internal class Biodata {

    internal class NoteColumns : BaseColumns {
        companion object {
            const val _ID = "_id"
            const val TABLE_NAME = "Biodata"
            const val NAMA = "Nama"
            const val Alamat = "Alamat"
            const val NIK = "NIK"
            const val Tempat_Lahir = "TempatLahir"
            const val Tanggal_Lahir = "TanggalLahir"
            const val DATE = "date"
        }
    }
}