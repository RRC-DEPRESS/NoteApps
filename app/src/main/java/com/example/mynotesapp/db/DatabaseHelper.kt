package com.example.mynotesapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mynotesapp.db.Biodata.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbnoteapp"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${Biodata.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${Biodata.NoteColumns.NAMA} TEXT NOT NULL," +
                " ${Biodata.NoteColumns.Alamat} TEXT NOT NULL," +
                " ${Biodata.NoteColumns.NIK} INTEGER NOT NULL," +
                " ${Biodata.NoteColumns.Tanggal_Lahir} TEXT NOT NULL," +
                " ${Biodata.NoteColumns.Tempat_Lahir} TEXT NOT NULL," +
                " ${Biodata.NoteColumns.DATE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}