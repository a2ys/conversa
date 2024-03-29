package dev.a2ys.conversa.authentication.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.a2ys.conversa.R
import dev.a2ys.conversa.databinding.ActivityInfoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dev.a2ys.conversa.main.activities.MainActivity
import dev.a2ys.conversa.models.BasicInfo
import dev.a2ys.conversa.models.User
import dev.a2ys.conversa.utils.AgeCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var uid: String
    private lateinit var database: DatabaseReference
    private lateinit var ageCalculator: AgeCalculator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Firebase.auth.currentUser
        user?.let {
            uid = it.uid
        }

        database = FirebaseDatabase.getInstance().reference
        ageCalculator = AgeCalculator()

        val formattedNumber = getString(R.string.phone_number_format, user!!.phoneNumber!!.substring(3, 13))
        binding.phoneNumber.editText!!.setText(formattedNumber)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        binding.dobSelector.setOnClickListener {
            datePicker.show(supportFragmentManager, "DATE_OF_BIRTH_PICKER")
            datePicker.addOnPositiveButtonClickListener {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = datePicker.selection!!

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val selectedDate = "${day}/${month}/${year}"

                binding.dateOfBirth.editText!!.setText(selectedDate)
            }
        }

        binding.submit.setOnClickListener {
            binding.submit.visibility = View.INVISIBLE
            binding.progressCircular.visibility = View.VISIBLE

            if (validateEntries()) {
                val username = binding.username.editText!!.text.toString()
                val dateOfBirth = binding.dateOfBirth.editText!!.text.toString()

                lifecycleScope.launch {
                    try {
                        val ageOfUser = calculateAge(dateOfBirth)
                        if (ageOfUser >= 16) {
                            val isUnique = checkUsernameUnique(username)

                            if (isUnique) {
                                val currentUser = User(
                                    uid,
                                    BasicInfo(
                                        binding.name.editText!!.text.toString(),
                                        binding.dateOfBirth.editText!!.text.toString(),
                                        binding.genderMenu.editText!!.text.toString(),
                                    ),
                                    username
                                )

                                lifecycleScope.launch {
                                    try {
                                        database.child("registeredUsers").child(uid).setValue(currentUser)

                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                        finish()
                                    } catch (e: Exception) {
                                        showError("An error occurred. Please try again.")
                                    } finally {
                                        binding.submit.visibility = View.VISIBLE
                                        binding.progressCircular.visibility = View.INVISIBLE
                                    }
                                }
                            } else {
                                showError("Username already taken!", "Try Again")

                                binding.submit.visibility = View.VISIBLE
                                binding.progressCircular.visibility = View.INVISIBLE
                            }
                        } else if (ageOfUser in 0..15) {
                            showError("You must be at least 16 years old!")
                        } else {
                            showError("You cannot be born in the future!", "😂")
                        }
                    } catch (e: Exception) {
                        showError("An error occurred. Please try again.", "Ok!")
                    } finally {
                        binding.submit.visibility = View.VISIBLE
                        binding.progressCircular.visibility = View.INVISIBLE
                    }
                }
            }

            binding.submit.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.INVISIBLE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (s) {
                    binding.name.editText?.text -> binding.name.error = null
                    binding.dateOfBirth.editText?.text -> binding.dateOfBirth.error = null
                    binding.genderMenu.editText?.text -> binding.genderMenu.error = null
                    binding.username.editText?.text -> binding.username.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.name.editText!!.addTextChangedListener(textWatcher)
        binding.dateOfBirth.editText!!.addTextChangedListener(textWatcher)
        binding.genderMenu.editText!!.addTextChangedListener(textWatcher)
        binding.username.editText!!.addTextChangedListener(textWatcher)
    }

    private fun validateEntries() : Boolean {
        val error = "This field cannot be empty"

        if (binding.name.editText!!.text.toString().isEmpty()) binding.name.error = error
        if (binding.dateOfBirth.editText!!.text.toString().isEmpty()) binding.dateOfBirth.error = error
        if (binding.genderMenu.editText!!.text.toString().isEmpty()) binding.genderMenu.error = error
        if (binding.username.editText!!.text.toString().isEmpty()) binding.username.error = error

        return binding.name.editText!!.text.toString().isNotEmpty() &&
                binding.dateOfBirth.editText!!.text.toString().isNotEmpty() &&
                binding.genderMenu.editText!!.text.toString().isNotEmpty() &&
                binding.username.editText!!.text.toString().isNotEmpty()
    }

    private suspend fun checkUsernameUnique(username: String): Boolean = withContext(Dispatchers.IO) {
        val usersRef = database.child("registeredUsers")
        val snapshot = usersRef.orderByChild("username").equalTo(username).get().await()
        snapshot.childrenCount == 0L
    }

    private suspend fun calculateAge(dateOfBirth: String): Int = withContext(Dispatchers.Default) {
        val dateOfBirthSplitted = dateOfBirth.split("/")
        ageCalculator.getAge(dateOfBirthSplitted[2].toInt(), dateOfBirthSplitted[1].toInt(), dateOfBirthSplitted[0].toInt())
    }

    private fun showError(message: String, action: String = "Got It!") {
        Snackbar.make(
            binding.submit,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setAction(action) {}
            .show()
    }
}