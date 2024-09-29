package com.example.opsc7312_wickedtech

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.example.opsc7312_wickedtech.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout
        val user = auth.currentUser


//        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, //, binding.toolbar
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

//        binding.logoutButton.setOnClickListener {
//            auth.signOut()
//            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
//            finish()
//        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_bmi -> {
                Toast.makeText(this,"BMI Clicked",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BMIActivity::class.java))
                finish()
            }

            R.id.nav_workout -> {
                Toast.makeText(this, "Workout Clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutActivity::class.java))
                finish()
            }
            R.id.nav_workout_list -> {
                Toast.makeText(this, "Workout Clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutListActivity::class.java))
                finish()
            }

            // Add more cases for other menu items
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}