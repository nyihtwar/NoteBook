package com.example.sqlitedatabase

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    var id=0
    var dbTable="Notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        var actionBar=supportActionBar
        actionBar!!.title="back"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        try {
            var bundle = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id != 0) {
                etTitle.setText(bundle.getString("name"))
                etDes.setText(bundle.getString("des"))
            }
        } catch (ex: Exception){}
    }
    fun buAdd(view: View){

        var dbManager=DbManager(this)
        var values=ContentValues()

        values.put("Title",etTitle.text.toString())
        values.put("Des",etDes.text.toString())

        if (id==0){

            val ID=dbManager.dataInsert(values)
            if(ID>0){
                Toast.makeText(this,"note is added",Toast.LENGTH_SHORT).show()
              finish()
            }else{
                Toast.makeText(this,"cannot add note",Toast.LENGTH_SHORT).show()
            }

        }else{
           var selectionArgs= arrayOf(id.toString())
            val ID=dbManager.dataUpdate(values,"ID=?",selectionArgs)

            if (ID>0){
                Toast.makeText(this,"note is added",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"cannot add note",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
