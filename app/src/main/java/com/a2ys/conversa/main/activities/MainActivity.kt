package com.a2ys.conversa.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}