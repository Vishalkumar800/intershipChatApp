package com.rach.intershipchatapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rach.intershipchatapp.R
import com.rach.intershipchatapp.adpters.ChatAdapters
import com.rach.intershipchatapp.databinding.FragmentChatBinding
import com.rach.intershipchatapp.model.userModel

class ChatFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding
    private var database : FirebaseDatabase? = null

    private lateinit var userList: ArrayList<userModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        database = FirebaseDatabase.getInstance()

        userList = ArrayList()

        binding.progressbar.visibility = View.VISIBLE

        database!!.reference.child("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    userList.clear()

                    for (doc in snapshot.children){

                        val user = doc.getValue(userModel::class.java)

                        if (user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }

                        binding.recyclerView.adapter = ChatAdapters(requireContext(), userList)
                        binding.progressbar.visibility = View.GONE


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progressbar.visibility = View.GONE

                    Toast.makeText(requireContext() ,"Something Went Wrong ",Toast.LENGTH_SHORT).show()



                }

            })

        return binding.root
    }


}