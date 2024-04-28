package com.rach.intershipchatapp.LoginAndProfileActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rach.intershipchatapp.MainActivity
import com.rach.intershipchatapp.R
import com.rach.intershipchatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding :ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){

            startActivity(Intent(this , MainActivity::class.java))
            finish()

        }

        binding.contiueButton.setOnClickListener {

            if (binding.phoneNumber.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter Phone Number First",Toast.LENGTH_SHORT).show()
            }else{
                var intent = Intent(this,OTPActivity::class.java)
                intent.putExtra("number",binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }

        }


    }
}