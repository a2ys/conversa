package dev.a2ys.conversa.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val uid: String? = auth.uid

        uid?.let {
            database.reference.child("registeredUsers").child(uid).child("onlineStatus").setValue(true)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_navigation)
        val navController = navHostFragment!!.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onResume() {
        val uid: String? = auth.uid

        uid?.let {
            database.reference.child("registeredUsers").child(uid).child("onlineStatus").setValue(true)
        }

        super.onResume()
    }

    override fun onStop() {
        val uid: String? = auth.uid

        uid?.let {
            database.reference.child("registeredUsers").child(uid).child("onlineStatus").setValue(false)
        }

        super.onStop()
    }
}