package com.noviherlambang.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etName: EditText
    private lateinit var etAlamat:EditText
    private lateinit var btnSave:Button
    private lateinit var ref :DatabaseReference
    private lateinit var mhList: MutableList<Mahasiswa>
    private  lateinit var listMhs:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase
            .getInstance("https://crud-app-56cd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("mahasiswa")

        etName = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        btnSave = findViewById(R.id.btn_save)
        listMhs = findViewById(R.id.lv_mhs)

        btnSave.setOnClickListener(this)
        mhList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    mhList.clear()
                    for (h in p0.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null) {
                            mhList.add(mahasiswa)
                        }
                    }

                    val adapter = MahasiswaAdapter(this@MainActivity, R.layout.item_mhs, mhList)
                        listMhs.adapter = adapter
                }
            }

        })
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val nama = etName.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()

        if (nama.isEmpty()){
            etName.error = "Isi Nama!"
            return
        }
        if (alamat.isEmpty()){
            etAlamat.error = "Isi Alamat"
            return
        }


        val mhsId = ref.push().key

        val mhs = Mahasiswa(mhsId,nama,alamat)

        if (mhsId != null) {
            ref.child(mhsId).setValue(mhs).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data Berhasil ditambahkan",Toast.LENGTH_SHORT).show()
            }
        }
    }

}