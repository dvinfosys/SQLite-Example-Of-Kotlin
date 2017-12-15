package net.dvinfosys.sqlitedemo

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.deletedilog.*

class MainActivity : AppCompatActivity() {

    lateinit var mySqlHalper:MySqlHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        MySqlHelper.getInstance(this)

        mySqlHalper= MySqlHelper(this)
        fab.setOnClickListener {
            val person = Person(100, "Dhaval", "Bhuva", 23)
            mySqlHalper.insert(person)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun deleteUser(v:View)
    {
        val personId=this.edittext_userid.text.toString()
       val dialogAlert=AlertDialog.Builder(this)
        val inflater=this.layoutInflater
        val dialogView=inflater.inflate(R.layout.deletedilog,null)
        dialogAlert.setView(dialogView)
        val cName=dialogView.findViewById<TextView>(R.id.tv_person_name)
        val cSurname=dialogView.findViewById<TextView>(R.id.tv_person_surname)
        val cAge=dialogView.findViewById<TextView>(R.id.tv_person_age)
        val viewPerson=mySqlHalper.readPerson(personId)
        viewPerson.forEach{
            //deleteAlert.setMessage("Name : "+it.name.toString()+"\nSurname : "+it.surname.toString()+"\nAge : "+it.age.toString())
           val personName=it.name.toString()
            val personSurname=it.surname.toString()
            val personAge=it.age.toString()
            cName.setText(personName)
            cSurname.setText(personSurname)
            cAge.setText(personAge)
        }

        val buttonYes=dialogView.findViewById<Button>(R.id.yes_button)
        buttonYes.setOnClickListener(){
            val result=mySqlHalper.deletePerson(personId.toString())
            if (result){
                Toast.makeText(applicationContext,"Delete Person Successfully...",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"Person Not Found...",Toast.LENGTH_SHORT).show()
            }
            this.textview_result.text = "Deleted Person : "+result

        }
        val buttoNo=dialogView.findViewById<Button>(R.id.no_button)
        buttoNo.setOnClickListener{
            Toast.makeText(this,"Peson No Delete...",Toast.LENGTH_SHORT).show()
        }

        dialogAlert.show()
        this.ll_entries.removeAllViews()
    }

    fun addPerson(v: View) {
        val id = this.edittext_userid.text.toString()
        val name = this.edittext_name.text.toString()
        val surname = this.edittext_surname.text.toString()
        val age = this.edittext_age.text.toString()
        val mainid = id.toInt()
        val mainage = age.toInt()
        val person = Person(mainid, name, surname, mainage)
        mySqlHalper.insert(person)
        this.edittext_age.setText("")
        this.edittext_name.setText("")
        this.edittext_userid.setText("")
        this.edittext_surname.setText("")
        this.textview_result.text = "Added user : "+person
        this.ll_entries.removeAllViews()

    }

    fun showAllUsers(v: View) {
        val person=mySqlHalper.readAllPerson()
        this.ll_entries.removeAllViews()
        person.forEach{
            val tvPerson=TextView(this)
            tvPerson.textSize=30f
            tvPerson.text=it.id.toString()+" - "+it.name.toString() +" "+ it.surname.toString() + " - " + it.age.toString()
            this.ll_entries.addView(tvPerson)
        }
        this.textview_result.text = "Fetched " + person.size + " users"
    }

}
