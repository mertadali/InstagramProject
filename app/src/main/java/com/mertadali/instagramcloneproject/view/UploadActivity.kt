package com.mertadali.instagramcloneproject.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.mertadali.instagramcloneproject.databinding.ActivityUploadBinding
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedPicture : Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestoreDatabase : FirebaseFirestore
    private lateinit var storage : FirebaseStorage




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()

         auth = Firebase.auth
        firestoreDatabase = Firebase.firestore
        storage = Firebase.storage



    }

    fun upload(view: View) {

        // Universal unique id  storage birden fazla görsel eklemek için.
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if (selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                // download url alıp -> Firestore kaydetme işlemi
                val uploadImageReference = storage.reference.child("images").child(imageName)
                uploadImageReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    if (auth.currentUser != null){
                        val postMap = hashMapOf<String,Any>()
                        postMap["downloadUrl"] = downloadUrl
                        postMap["userEmail"] = auth.currentUser!!.email!!
                        postMap["comment"] = binding.commentText.text.toString()
                        postMap["date"] = Timestamp.now()

                        firestoreDatabase.collection("Posts").add(postMap).addOnSuccessListener {
                            finish()

                        }.addOnFailureListener {Exeption ->
                            Toast.makeText(this@UploadActivity,Exeption.localizedMessage,Toast.LENGTH_LONG).show()
                        }


                    }



                }.addOnFailureListener {
                    Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }


            }.addOnFailureListener {
                Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }

    }

    fun selectImage(view: View) {
        // Galeriye gitmek için izin gerekli
        if (Build.VERSION.SDK_INT == 28){
            if (ContextCompat.checkSelfPermission(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    // permission request
                    Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission",View.OnClickListener {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    }).show()
                }else{
                    // permission request
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }else{
                // permission granted - ContextCompat
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }


    }
    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == RESULT_OK){
            val intentFromResult = result.data
            if (intentFromResult != null){
               selectedPicture =  intentFromResult.data
                selectedPicture?.let {
                    binding.imageView.setImageURI(it)
                }
            }

        }else if (result.resultCode == RESULT_CANCELED){
            Toast.makeText(this@UploadActivity,"Permission needed for galllery",Toast.LENGTH_LONG).show()
        }

        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                // permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }

    }



}