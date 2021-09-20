package com.noviherlambang.crudapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MahasiswaAdapter(val mCtx:Context,val layoutResId:Int,val mhsList:List<Mahasiswa>):ArrayAdapter<Mahasiswa>(mCtx,layoutResId,mhsList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)

        val view :View = layoutInflater.inflate(layoutResId, null )

        val tvNama :TextView = view.findViewById(R.id.tv_nama)
        val tvAlamat :TextView = view.findViewById(R.id.tv_alamat)
        val tvEdit :TextView = view.findViewById(R.id.tv_edit)

        val mahasiswa = mhsList[position]

        tvEdit.setOnClickListener {
            showUpdateDialog(mahasiswa)
        }

        tvNama.text = mahasiswa.nama
        tvAlamat.text = mahasiswa.alamat

        return view
    }

    private fun showUpdateDialog(mahasiswa: Mahasiswa) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Edit Data")
        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view :View = layoutInflater.inflate(R.layout.update_dialog, null )

        val etNama:EditText = view.findViewById(R.id.et_nama)
        val etAlamat:EditText = view.findViewById(R.id.et_alamat)

        etNama.setText(mahasiswa.nama)
        etAlamat.setText(mahasiswa.alamat)

        builder.setView(view)
        builder.setPositiveButton("Update"){p0,p1 ->
            val ref = FirebaseDatabase
                .getInstance("https://crud-app-56cd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("mahasiswa")

            val nama = etNama.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()

            if (nama.isEmpty()){
                etNama.error = "Isi Nama!"
                etNama.requestFocus()
                return@setPositiveButton
            }
            if (alamat.isEmpty()){
                etAlamat.error = "Isi Alamat"
                etAlamat.requestFocus()
                return@setPositiveButton
            }
            val mahasiswa = Mahasiswa(mahasiswa.id, nama, alamat)
            ref.child(mahasiswa.id!!).setValue(mahasiswa)
            Toast.makeText(mCtx, "Data Berhasil di Update", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("No"){p0,p1->

        }
        builder.setNegativeButton("Delete"){p0,p1->
            val ref = FirebaseDatabase
                .getInstance("https://crud-app-56cd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("mahasiswa").child(mahasiswa.id!!)

            ref.removeValue()
            Toast.makeText(mCtx, "Data Berhasil di Delete", Toast.LENGTH_SHORT).show()
        }


        val alert = builder.create()
        alert.show()
    }
}