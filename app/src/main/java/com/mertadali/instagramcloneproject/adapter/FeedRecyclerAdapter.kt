package com.mertadali.instagramcloneproject.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mertadali.instagramcloneproject.databinding.RecyclerRowBinding
import com.mertadali.instagramcloneproject.model.Post
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class FeedRecyclerAdapter(private  val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {



    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)


    }

    override fun getItemCount(): Int {
        return postList.size



    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.binding.recyclerEmailText.text = postList[position].userEmail
        holder.binding.recyclerCommentText.text = postList[position].comment

        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.recyclerImageView)


    }




}



