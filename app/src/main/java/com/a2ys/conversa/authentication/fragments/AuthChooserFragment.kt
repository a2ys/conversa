package com.a2ys.conversa.authentication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.FragmentAuthChooserBinding
import com.google.android.material.snackbar.Snackbar

class AuthChooserFragment : Fragment() {

    private lateinit var binding: FragmentAuthChooserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthChooserBinding.inflate(layoutInflater, container, false)

        binding.emailAuthButton.setOnClickListener {
            Snackbar.make(
                requireActivity().findViewById(R.id.user_authentication_navigation_fragment),
                "This feature will be implemented soon!",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction("Sure!") {}
                .show();
        }

        binding.phoneAuthButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.user_authentication_navigation_fragment).navigate(R.id.action_authChooserFragment_to_phoneNumberFragment)
        }

        return binding.root
    }
}