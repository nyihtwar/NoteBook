package com.example.sqlitedatabase

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    val listOfNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         loadQuery("%")
        supportActionBar!!.title="NoteBook"
    }

    override fun onResume() {
        loadQuery("%")
        super.onResume()
    }
    fun loadQuery(title: String) {

        var dbManager=DbManager(this)
        val projections= arrayOf("ID","Title","Des")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.dataQuery(projections,"Title Like ?",selectionArgs,"Title")

        listOfNotes.clear()

        if (cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Des=cursor.getString(cursor.getColumnIndex("Des"))

                listOfNotes.add(Note(ID,Title,Des))
            }while (cursor.moveToNext())
        }

        var myNoteAdapter=MyNoteAdapter(this,listOfNotes)
        lvNotes.adapter=myNoteAdapter
    }
    inner class MyNoteAdapter:BaseAdapter{
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
           var myView=LayoutInflater.from(context).inflate(R.layout.ticket,null)
            var myNote=listNoteAdapter[p0]
            myView.tvTitle.text=myNote.noteTitle
            myView.tvDes.text=myNote.noteDes

            myView.ivDelete.setOnClickListener {
                var db=DbManager(this.context!!)
                var selectionArgs= arrayOf(myNote.noteId.toString())
                db.dataDelete("ID=?",selectionArgs)
                loadQuery("%")
            }
           myView.ivEdit.setOnClickListener {
               goUpdate(myNote)
           }
            return  myView
        }

        override fun getItem(p0: Int): Any {
            return listNoteAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
           return listNoteAdapter.size
        }

        var listNoteAdapter=ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context,listNoteAdapter:ArrayList<Note>):super(){
            this.listNoteAdapter=listNoteAdapter
            this.context=context
        }
    }
    fun goUpdate(note:Note){
        var intent= Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.noteId)
        intent.putExtra("name",note.noteTitle)
        intent.putExtra("des",note.noteDes)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity,query, Toast.LENGTH_LONG).show()
                loadQuery("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.addNote->{
                    var intent= Intent(this@MainActivity,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
