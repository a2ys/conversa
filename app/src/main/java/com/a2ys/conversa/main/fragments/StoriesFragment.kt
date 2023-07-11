package com.a2ys.conversa.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.FragmentChatsBinding
import com.a2ys.conversa.databinding.FragmentStoriesBinding

class StoriesFragment : Fragment() {

    private lateinit var binding: FragmentStoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoriesBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}