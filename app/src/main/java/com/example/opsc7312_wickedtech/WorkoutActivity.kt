package com.example.opsc7312_wickedtech

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.opsc7312_wickedtech.Activities.LoginActivity
import com.example.opsc7312_wickedtech.databinding.ActivityWorkoutBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class WorkoutActivity :  AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout
        val user = auth.currentUser
        // Check if user is null
        if (user == null) {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java)) // Assuming you have a LoginActivity
            finish()
            return
        }


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
            R.id.nav_questionaire -> {
                Toast.makeText(this,"Questionaire Clicked", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_workout -> {
                Toast.makeText(this,"Workout Clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutActivity::class.java))
                finish()

            }
            // Add more cases for other menu items
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}