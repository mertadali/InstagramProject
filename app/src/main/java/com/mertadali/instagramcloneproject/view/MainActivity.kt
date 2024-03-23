package com.mertadali.instagramcloneproject.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mertadali.instagramcloneproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private var trackBoolean : Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        // Aktif kullanıcı var mı sorgusu

        sharedPreferences = this.getSharedPreferences("com.mertadali.instagramcloneproject.view", MODE_PRIVATE)
        trackBoolean = false

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)


        if (isLoggedIn){
            val currentUser = auth.currentUser
            if (currentUser != null){
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }

        }




    }

      fun signIn(view: View){
          val rememberMeCheckbox = binding.checkBox

          val email = binding.username.text.toString()
          val password = binding.password.text.toString()
          val rememberMe = rememberMeCheckbox.isChecked

          if (rememberMe){
              with(sharedPreferences.edit()) {
                  putBoolean("isLoggedIn", true)
                  apply()
              }
          }

          if (email == "" || password == ""){
              Toast.makeText(this@MainActivity,"Your email or password is empty",Toast.LENGTH_LONG).show()
          }else{
              auth.signInWithEmailAndPassword(email,password)
                  .addOnSuccessListener {
                      val intent = Intent(this@MainActivity, FeedActivity::class.java)
                      startActivity(intent)
                      finish()

                  }.addOnFailureListener {exception ->
                      Toast.makeText(this@MainActivity,exception.localizedMessage,Toast.LENGTH_LONG).show()
                  }
          }

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
                   val intent = Intent(this@MainActivity, FeedActivity::class.java)
                   startActivity(intent)
                   finish()

               }.addOnFailureListener {
                   Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()

               }

       }


    }




}