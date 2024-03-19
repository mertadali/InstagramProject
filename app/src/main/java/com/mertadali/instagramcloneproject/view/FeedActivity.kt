package com.mertadali.instagramcloneproject.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


import com.mertadali.instagramcloneproject.R
import com.mertadali.instagramcloneproject.adapter.FeedRecyclerAdapter
import com.mertadali.instagramcloneproject.databinding.ActivityFeedBinding
import com.mertadali.instagramcloneproject.model.Post
import java.util.*
import kotlin.collections.ArrayList

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreDatabase: FirebaseFirestore
    private lateinit var feedAdapter : FeedRecyclerAdapter
    var postArrayList : ArrayList<Post> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        firestoreDatabase = Firebase.firestore
        getData()

        feedAdapter = FeedRecyclerAdapter(postArrayList)

        binding.recyclerView.adapter = feedAdapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)





    }

    // verileri alma

    private fun getData() {
        // Filtreleme işlemi için where ya da orderBy kullanılır
        firestoreDatabase.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this@FeedActivity, error.localizedMessage, Toast.LENGTH_LONG).show()

            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        postArrayList.clear()
                        val documents = value.documents

                        for (document in documents) {
                            // casting
                            val comment = document.get("comment") as String
                            val downlaodUrl = document.get("downloadUrl") as String
                            val userEmail = document.get("userEmail") as String

                            val post = Post(userEmail, downlaodUrl, comment)

                            postArrayList.add(post)
                        }
                        feedAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post){
            val intent = Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)

        }else if(item.itemId == R.id.signout){
            auth.signOut()
            val  intent = Intent(this@FeedActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }
}

