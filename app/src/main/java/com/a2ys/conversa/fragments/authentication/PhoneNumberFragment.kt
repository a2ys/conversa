package com.a2ys.conversa.fragments.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2ys.conversa.databinding.FragmentPhoneNumberBinding

class PhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneNumberBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}