package com.mertadali.instagramcloneproject

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mertadali.instagramcloneproject.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var selectedPicture : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()



    }

     fun upload(view: View) {

    }

     fun selectImage(view: View) {
        // Galeriye gitmek için izin gerekli
        if (ContextCompat.checkSelfPermission(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(binding.root,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission!",View.OnClickListener {
                    // permission launcher - request permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }else{
                // permission request - ActivityCompat
              permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }else{
            // permission granted - ContextCompat
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
           activityResultLauncher.launch(intentToGallery)


        }


    }

    private fun registerLauncher(){
       activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
           if (result.resultCode ==  RESULT_OK){
            val intentFromResult = result.data
               if (intentFromResult != null){
                  selectedPicture =  intentFromResult.data
                   selectedPicture?.let {Uri ->
                       binding.imageView.setImageURI(Uri)
                   }
               }

           }else if (result.resultCode == RESULT_CANCELED){

           }

       }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                // permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }else{
                // permission denied
                Toast.makeText(this@UploadActivity,"Permission needed",Toast.LENGTH_LONG).show()
            }
        }
    }







}