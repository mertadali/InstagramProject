package com.mertadali.instagramcloneproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mertadali.instagramcloneproject.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}