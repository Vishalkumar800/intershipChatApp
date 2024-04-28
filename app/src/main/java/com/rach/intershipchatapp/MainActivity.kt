package com.rach.intershipchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.rach.intershipchatapp.LoginAndProfileActivity.LoginActivity
import com.rach.intershipchatapp.adpters.viewPagerAdapters
import com.rach.intershipchatapp.databinding.ActivityMainBinding
import com.rach.intershipchatapp.ui.CallFragment
import com.rach.intershipchatapp.ui.ChatFragment
import com.rach.intershipchatapp.ui.StatusFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        val adapters = viewPagerAdapters(this , supportFragmentManager ,fragmentArrayList)

        binding.viewPager.adapter = adapters

        binding.tabsLayout.setupWithViewPager(binding.viewPager)


        drawerLayout = binding.drawerLayout

        val navigationView = findViewById<NavigationView>(R.id.navView)

        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav,R.string.closed_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()




    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            


        }
        return true



    }

}