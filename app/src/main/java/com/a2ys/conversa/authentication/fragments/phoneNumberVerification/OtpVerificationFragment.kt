package com.a2ys.conversa.authentication.fragments.phoneNumberVerification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.FragmentOtpVerificationBinding
import com.a2ys.conversa.landingpage.LandingPageActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OtpVerificationFragment : Fragment() {

    private lateinit var binding: FragmentOtpVerificationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val defaultValue = "NULL"
        val verificationId = sharedPref.getString("ver_id", defaultValue)
        val phoneNumber = sharedPref.getString("number", defaultValue)

        val navController = Navigation.findNavController(requireActivity(), R.id.user_authentication_navigation)

        binding.phn.text = "+91 $phoneNumber"

        binding.change.setOnClickListener {
            navController.navigateUp()
        }

        binding.submit.setOnClickListener {
            binding.submit.visibility = View.GONE
            binding.progressCircular.visibility = View.VISIBLE

            if (binding.otp.editText!!.text.trim().toString().length < 6) {
                Snackbar.make(requireActivity().findViewById(R.id.container), "Please enter a valid OTP!", Snackbar.LENGTH_SHORT)
                    .setAction("Got it") {}
                    .show()

                binding.submit.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
            } else {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, binding.otp.editText!!.text.trim().toString())
                signInWithPhoneAuthCredential(credential)
            }

        }

        return binding.root
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.submit.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE

                    val intent = Intent(requireContext(), LandingPageActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(requireActivity().findViewById(R.id.container), "Invalid code!", Snackbar.LENGTH_SHORT)
                            .setAction("Got It") {}
                            .show()

                        binding.submit.visibility = View.VISIBLE
                        binding.progressCircular.visibility = View.GONE
                    }
                }
            }
    }
}