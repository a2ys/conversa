package dev.a2ys.conversa.authentication.fragments.phoneNumberVerification

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
import dev.a2ys.conversa.landingpage.activities.LandingPageActivity
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
        val verificationId = sharedPref.getString("ver_id", null)
        val phoneNumber = sharedPref.getString("number", null)

        binding.phn.text = "+91 $phoneNumber"

        binding.change.setOnClickListener {
            navigateToPhoneNumberFragment()
        }

        binding.submit.setOnClickListener {
            val otp = binding.otp.editText!!.text.trim().toString()

            if (otp.length != 6) {
                showError("Please enter a valid OTP!")
            } else {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                signInWithPhoneAuthCredential(credential)
            }
        }

        return binding.root
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding.submit.visibility = View.GONE
        binding.progressCircular.visibility = View.VISIBLE

        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    navigateToLandingPage()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showError("Invalid code!")
                        binding.submit.visibility = View.VISIBLE
                        binding.progressCircular.visibility = View.GONE
                    }
                }
            }
    }

    private fun showError(message: String) {
        Snackbar.make(requireActivity().findViewById(R.id.container), message, Snackbar.LENGTH_SHORT)
            .setAction("Got it") {}
            .show()
    }

    private fun navigateToPhoneNumberFragment() {
        Navigation.findNavController(requireActivity(), R.id.user_authentication_navigation_fragment)
            .navigateUp()
    }

    private fun navigateToLandingPage() {
        startActivity(Intent(requireContext(), LandingPageActivity::class.java))
        requireActivity().finish()
    }
}