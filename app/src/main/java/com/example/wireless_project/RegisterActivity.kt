package com.example.wireless_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), CustomDialog.OnInputListener{
    private val db = Firebase.firestore
    companion object{
        fun getLaunchIntent(from: Context, name : String) : Intent {
            var intent: Intent =  Intent(from, RegisterActivity::class.java).apply {
                putExtra("name", name)
            }
            return intent
        }
    }
    var clickText = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUp()
        }
    private fun setUp(){
        textView6.setOnClickListener {
            clickText = 6
            onClick()
        }
        textView8.setOnClickListener {
            clickText = 8
            onClick()
        }
        register.setOnClickListener {
            setData()
        }
    }
    private fun setData(){
        val information = hashMapOf(
            "weight" to textView8.text.toString(),
            "height" to textView6.text.toString()
        )
        var name =""
        name = intent.getStringExtra("name")
        db.collection("users").document(name)
            .set(information)
            .addOnSuccessListener { Log.d("Success", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Fail", "Error writing document", e) }
        startActivity(MainActivity.getLaunchIntent(this))
    }
    private fun onClick(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val dialogFragment = CustomDialog()
        dialogFragment.show(fragmentTransaction, "dialog")
    }


    override fun sendInput(input: String?) {
        if(clickText==6){
            textView6.text = input
        }
        else if(clickText ==8){
            textView8.text = input
        }
    }

}