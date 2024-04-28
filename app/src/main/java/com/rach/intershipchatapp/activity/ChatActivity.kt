package com.rach.intershipchatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rach.intershipchatapp.R
import com.rach.intershipchatapp.adpters.MessageAdapters
import com.rach.intershipchatapp.databinding.ActivityChatBinding
import com.rach.intershipchatapp.model.messageModel
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var database: FirebaseDatabase

    private lateinit var senderUid: String

    private lateinit var receiverUid: String

    private lateinit var senderRoom: String

    private lateinit var receiverRoom: String

    private lateinit var list: ArrayList<messageModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        senderUid = FirebaseAuth.getInstance().uid.toString()

        receiverUid = intent.getStringExtra("uid")!!

        senderRoom = senderUid + receiverUid

        receiverRoom = receiverUid + senderUid

        database = FirebaseDatabase.getInstance()

        binding.imageView2.setOnClickListener {

            if (binding.mesageBox.text.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Message", Toast.LENGTH_SHORT).show()
            } else {

                val message =
                    messageModel(binding.mesageBox.text.toString(), senderUid, Date().time)

                val randomKey = database.reference.push().key

                database.reference.child("Chats").child(senderRoom)
                    .child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {

                        database.reference.child("Chats").child(receiverRoom)
                            .child("message").child(randomKey!!).setValue(message)
                            .addOnSuccessListener {

                                binding.mesageBox.text = null
                                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
                            }

                    }

            }

        }

        database.reference.child("Chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear()

                    for (doc in snapshot.children) {
                        val data = doc.getValue(messageModel::class.java)
                        list.add(data!!)
                    }

                    binding.recyclerView.adapter = MessageAdapters(this@ChatActivity, list)

                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(this@ChatActivity,"Something Went Wrong Bhai", Toast.LENGTH_SHORT).show()

                }

            })
    }
}