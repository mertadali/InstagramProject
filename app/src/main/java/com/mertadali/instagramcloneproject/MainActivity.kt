package com.mertadali.instagramcloneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mertadali.instagramcloneproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()




    }

    private  fun login(view: View){


    }

    fun signUp(view: View){

        val email = binding.username.text.toString()
        val password = binding.password.text.toString()

       if (email == "" || password == ""){
           Toast.makeText(this,"Yor email or password empty",Toast.LENGTH_LONG).show()
       }else{
           auth.createUserWithEmailAndPassword(email,password)
       // Firebase sunucularına istedik atılıyor bir mesaj alındığında intent yapılmalı yani asyc işlem yapmalıyız.
               .addOnSuccessListener {
                   val intent = Intent(this@MainActivity,FeedActivity::class.java)
                   startActivity(intent)
                   finish()

               }.addOnFailureListener {
                   Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()

               }

       }


    }




}