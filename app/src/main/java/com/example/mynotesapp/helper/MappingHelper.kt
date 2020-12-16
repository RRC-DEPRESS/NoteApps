package com.example.mynotesapp.helper

import android.database.Cursor
import com.example.mynotesapp.db.Biodata
import com.example.mynotesapp.entity.Note


import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Biodata.NoteColumns._ID))
                val NAMA = getString(getColumnIndexOrThrow(Biodata.NoteColumns.NAMA))
                val Alamat = getString(getColumnIndexOrThrow(Biodata.NoteColumns.Alamat))
                val NIK = getString(getColumnIndexOrThrow(Biodata.NoteColumns.NIK))
                val TempatLahir = getString(getColumnIndexOrThrow(Biodata.NoteColumns.Tempat_Lahir))
                val TanggalLahir = getString(getColumnIndexOrThrow(Biodata.NoteColumns.Tanggal_Lahir))
                val date = getString(getColumnIndexOrThrow(Biodata.NoteColumns.DATE))
                notesList.add(Note(id, NAMA, Alamat, NIK,TempatLahir, TanggalLahir, date))
            }
        }
        return notesList
    }
}