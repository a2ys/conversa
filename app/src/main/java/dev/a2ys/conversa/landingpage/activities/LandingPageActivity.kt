package dev.a2ys.conversa.landingpage.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a2ys.conversa.databinding.ActivityLandingPageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dev.a2ys.conversa.authentication.activities.InfoActivity
import dev.a2ys.conversa.authentication.activities.UserAuthenticationActivity
import dev.a2ys.conversa.main.activities.MainActivity

class LandingPageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityLandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateUser(currentUser.uid)
        } else {
            navigateToAuthenticationActivity()
        }
    }

    private fun navigateUser(uid: String) {
        database.child("registeredUsers").child(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.exists()) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(applicationContext, InfoActivity::class.java))
                    finish()
                }
            } else {
                showErrorSnackbar()
            }
        }
    }

    private fun navigateToAuthenticationActivity() {
        startActivity(Intent(applicationContext, UserAuthenticationActivity::class.java))
        finish()
    }

    private fun showErrorSnackbar() {
        Snackbar.make(binding.root, "Please contact the developer!", Snackbar.LENGTH_SHORT)
            .setAction("Got it") {}
            .show()
    }
}