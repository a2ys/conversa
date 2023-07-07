package com.a2ys.conversa.authentication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.a2ys.conversa.databinding.FragmentAuthChooserBinding

class AuthChooserFragment : Fragment() {

    private lateinit var binding: FragmentAuthChooserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthChooserBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}