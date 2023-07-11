package com.a2ys.conversa.landingpage.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a2ys.conversa.R
import com.a2ys.conversa.authentication.activities.InfoActivity
import com.a2ys.conversa.authentication.activities.UserAuthenticationActivity
import com.a2ys.conversa.databinding.ActivityLandingPageBinding
import com.a2ys.conversa.main.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LandingPageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityLandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val user = Firebase.auth.currentUser
            user?.let {
                uid = it.uid
            }

            database = FirebaseDatabase.getInstance().reference

            database.child("registeredUsers").child(uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.exists()) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(applicationContext, InfoActivity::class.java))
                        finish()
                    }
                } else {
                    Snackbar.make(findViewById(R.id.container), "Please contact the developer!", Snackbar.LENGTH_SHORT)
                        .setAction("Got it") {}
                        .show()
                }
            }
        } else {
            startActivity(Intent(applicationContext, UserAuthenticationActivity::class.java))
            finish()
        }
    }
}