package com.example.mynotesapp

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mynotesapp.db.Biodata
import com.example.mynotesapp.db.BiodataHelper
import com.example.mynotesapp.entity.Note
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var noteHelper: BiodataHelper

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        noteHelper = BiodataHelper.getInstance(applicationContext)
        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            note?.let {
                edt_NAMA.setText(it.NAMA)
                edt_Alamat.setText(it.Alamat)
                edt_NIK.setText(it.NIK)
                edt_Tempat_Lahir.setText(it.TempatLahir)
                edt_Tanggal_Lahir.setText(it.TanggalLahir)
            }

        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle

        btn_submit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val NAMA = edt_NAMA.text.toString().trim()
            val Alamat = edt_Alamat.text.toString().trim()
            val NIK = edt_NIK.text.toString().trim()
            val TempatLahir = edt_Tempat_Lahir.text.toString().trim()
            val TanggalLahir = edt_Tanggal_Lahir.text.toString().trim()

            /*
            Jika fieldnya masih kosong maka tampilkan error
             */
            if (title.isEmpty()) {
                edt_NAMA.error = "Field can not be blank"
                return
            }

            note?.NAMA = NAMA
            note?.Alamat = Alamat
            note?.NIK = NIK
            note?.TempatLahir = TempatLahir
            note?.TanggalLahir = TanggalLahir

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            // Gunakan contentvalues untuk menampung data
            val values = ContentValues()
            values.put(Biodata.NoteColumns.NAMA, NAMA)
            values.put(Biodata.NoteColumns.Alamat, Alamat)
            values.put(Biodata.NoteColumns.NIK, NIK)
            values.put(Biodata.NoteColumns.Tempat_Lahir, TempatLahir)
            values.put(Biodata.NoteColumns.Tanggal_Lahir, TanggalLahir)

            /*
            Jika merupakan edit maka setresultnya UPDATE, dan jika bukan maka setresultnya ADD
            */
            if (isEdit) {
                val result = noteHelper.update(note?.id.toString(), values)
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else {
                note?.date = getCurrentDate()
                values.put(Biodata.NoteColumns.DATE, getCurrentDate())
                val result = noteHelper.insert(values)

                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }


    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    deleteNote = 20
     */
    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Note"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@NoteAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}