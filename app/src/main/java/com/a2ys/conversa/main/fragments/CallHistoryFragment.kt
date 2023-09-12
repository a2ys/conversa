package com.a2ys.conversa.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2ys.conversa.databinding.FragmentCallHistoryBinding

class CallHistoryFragment : Fragment() {

    private lateinit var binding: FragmentCallHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallHistoryBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}