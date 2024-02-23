package com.mertadali.instagramcloneproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mertadali.instagramcloneproject.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

    private fun upload(view: View) {

    }

    private fun selectImage(view: View) {

    }






}