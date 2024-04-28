package com.rach.intershipchatapp.LoginAndProfileActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.rach.intershipchatapp.MainActivity
import com.rach.intershipchatapp.R
import com.rach.intershipchatapp.databinding.ActivityProfileBinding
import com.rach.intershipchatapp.model.userModel
import java.util.Date

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImage: Uri
    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Wait")
        builder.setTitle("Updating")
        builder.setCancelable(false)

        dialog = builder.create()

        database = FirebaseDatabase.getInstance()

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.profileImage.setOnClickListener {

            val intent = Intent ()

            intent.action = Intent.ACTION_GET_CONTENT
            intent.type ="image/*"

            startActivityForResult(intent ,1 )

        }

        binding.saveAndcontinmue.setOnClickListener {


            if (binding.userName.text!!.isEmpty()){

                Toast.makeText(this,"Please Enter Profile Name First",Toast.LENGTH_SHORT).show()
            }else if (
                selectedImage == null

            ){
                Toast.makeText(this ,"Please first profile Image First",Toast.LENGTH_SHORT).show()

            }else{
                storeData()
            }

        }

    }

    private fun storeData() {
        dialog.show()

        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener {
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->

                    uploadInfo(task.toString())

                }
            }
        }

    }

    private fun uploadInfo(imageUrl: String) {

        dialog.show()

        val user = userModel(auth.uid.toString() , binding.userName.text.toString() , auth.currentUser!!.phoneNumber.toString() , imageUrl)
        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this,"Profile Saved",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){

            if (data.data != null){
                selectedImage = data.data!!

                binding.profileImage.setImageURI(selectedImage)
            }

        }
    }
}