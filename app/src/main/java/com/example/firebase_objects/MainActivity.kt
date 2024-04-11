package com.example.firebase_objects

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private  lateinit var rootNode : FirebaseDatabase
    private  lateinit var userReference : DatabaseReference
    private  lateinit var listView :ListView
    private var counter: Int? = 0
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lsOutput)
        rootNode = FirebaseDatabase.getInstance()
        userReference = rootNode.getReference("users")

        // this is for user inputs
        val btn_click_me = findViewById(R.id.btnData) as Button
        btn_click_me.setOnClickListener {
            val data1 : EditText = findViewById(R.id.txtData1) // name
            val data2 : EditText = findViewById(R.id.txtData2) // food
            if (counter == null){
                count == 0
            }
            else{
                count == counter
                count++
            }
            val dc = DataClass(data1.text.toString(), data2.text.toString(), count)
            userReference.child(dc.id.toString()).setValue(dc)
        }

        val list = ArrayList<String>()
        val adapter = ArrayAdapter(this, R.layout.listitems,list)
        listView.adapter=adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position)
            var id = element.toString().take(3).trim()
            val docRef = userReference.child(id).removeValue()
        }

        userReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                list.clear()
                counter = 0
                for(snapshot1 in snapshot.children){
                    val dc2 = snapshot1.getValue(DataClass::class.java)
                    val txt = " ${dc2?.id}      Name is ${dc2?.nane} , favourite food: ${dc2?.description}"
                    txt?.let {list.add(it)}
                    counter = dc2?.id
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error:DatabaseError){

            }
        })
    }
}