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
import dev.a2ys.conversa.main.activities.MainActivity
import com.a2ys.conversa.databinding.FragmentPhoneNumberBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class PhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneNumberBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth

        binding.submit.setOnClickListener {
            binding.submit.visibility = View.GONE
            binding.progressCircular.visibility = View.VISIBLE

            if (binding.phoneNumber.editText!!.text.trim().toString().length < 10) {
                Snackbar.make(
                    requireActivity().findViewById(R.id.container),
                    "Please enter a valid phone number!",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Got it") {}
                    .show()

                binding.submit.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
            } else {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91" + binding.phoneNumber.editText!!.text.trim().toString())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

        return binding.root
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Snackbar.make(requireActivity().findViewById(R.id.container), "Please check the credentials provided!", Snackbar.LENGTH_SHORT)
                        .setAction("Got it") {}
                        .show()
                }
                is FirebaseTooManyRequestsException -> {
                    Snackbar.make(requireActivity().findViewById(R.id.container), "Please contact the developer!", Snackbar.LENGTH_SHORT)
                        .setAction("Got it") {}
                        .show()
                }
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Snackbar.make(requireActivity().findViewById(R.id.container), "OTP has been sent!", Snackbar.LENGTH_SHORT)
                .setAction("Got it") {}
                .show()

            binding.submit.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE

            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString("ver_id", verificationId)
                putString("number", binding.phoneNumber.editText!!.text.trim().toString())
                apply()
            }

            Navigation.findNavController(requireActivity(), R.id.user_authentication_navigation_fragment).navigate(R.id.action_phoneNumberFragment_to_otpVerificationFragment)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.submit.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(requireActivity().findViewById(R.id.container), "Invalid code!", Snackbar.LENGTH_SHORT)
                            .setAction("Got it") {}
                            .show()

                        binding.submit.visibility = View.VISIBLE
                        binding.progressCircular.visibility = View.GONE
                    }
                }
            }
    }
}