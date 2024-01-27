package dev.a2ys.conversa.authentication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a2ys.conversa.databinding.ActivityUserAuthenticationBinding

class UserAuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserAuthenticationBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}