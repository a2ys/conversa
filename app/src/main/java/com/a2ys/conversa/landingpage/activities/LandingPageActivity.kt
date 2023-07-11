package com.a2ys.conversa.landingpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.ActivityLandingPageBinding
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
                        startActivity(Intent(applicationContext, ConnectorActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(applicationContext, "Contact the developer!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            startActivity(Intent(applicationContext, InitialActivity::class.java))
            finish()
        }
    }
}