package dev.a2ys.conversa.authentication.fragments.phoneNumberVerification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import dev.a2ys.conversa.R
import dev.a2ys.conversa.databinding.FragmentPhoneNumberBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dev.a2ys.conversa.main.activities.MainActivity
import java.util.concurrent.TimeUnit

class PhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneNumberBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.submit.setOnClickListener {
            val phoneNumber = binding.phoneNumber.editText!!.text.trim().toString()

            if (phoneNumber.length < 10) {
                showError("Please enter a valid phone number!")
            } else {
                startPhoneNumberVerification("+91$phoneNumber")
            }
        }

        return binding.root
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()

        binding.submit.visibility = View.GONE
        binding.progressCircular.visibility = View.VISIBLE

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> showError("Please check the credentials provided!")
                is FirebaseTooManyRequestsException -> showError("Please contact the developer!")
                else -> showError("An error occurred. Please try again.")
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("ver_id", verificationId)
                putString("number", binding.phoneNumber.editText!!.text.trim().toString())
                apply()
            }

            navigateToOtpVerificationFragment()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                    showError("Invalid code!")
                    binding.submit.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                }
            }
    }

    private fun showError(message: String) {
        Snackbar.make(requireActivity().findViewById(R.id.container), message, Snackbar.LENGTH_SHORT)
            .setAction("Got it") {}
            .show()
    }

    private fun navigateToOtpVerificationFragment() {
        Navigation.findNavController(requireActivity(), R.id.user_authentication_navigation_fragment)
            .navigate(R.id.action_phoneNumberFragment_to_otpVerificationFragment)
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }
}