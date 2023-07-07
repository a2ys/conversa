package com.a2ys.conversa.authentication.fragments.phoneNumberVerification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2ys.conversa.databinding.FragmentOtpVerificationBinding

class OtpVerificationFragment : Fragment() {

    private lateinit var binding: FragmentOtpVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}