package dev.a2ys.conversa.authentication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.a2ys.conversa.databinding.ActivityInfoBinding
import dev.a2ys.conversa.main.activities.MainActivity
import dev.a2ys.conversa.models.User
import dev.a2ys.conversa.utils.AgeCalculator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
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

        binding.phoneNumber.editText!!.setText("+91 ${user!!.phoneNumber!!.substring(3, 13)}")

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
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
                val currentUser = User(binding.name.editText!!.text.toString(),
                    binding.dateOfBirth.editText!!.text.toString(),
                    binding.genderMenu.editText!!.text.toString())

                val dateOfBirthSplitted = binding.dateOfBirth.editText!!.text.toString().split("/")
                val ageOfUser = ageCalculator.getAge(dateOfBirthSplitted[2].toInt(), dateOfBirthSplitted[1].toInt(), dateOfBirthSplitted[0].toInt())

                if (ageOfUser >= 16) {
                    database.child("registeredUsers").child(uid).setValue(currentUser)

                    binding.submit.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE

                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else if (ageOfUser in 0..15) {
                    Snackbar.make(binding.rootView,
                        "You must be at least 16 years old!",
                        Snackbar.LENGTH_LONG)
                        .setAction("Got It!") {}
                        .show()
                } else {
                    Snackbar.make(binding.rootView,
                        "You cannot be born in the future!",
                        Snackbar.LENGTH_LONG)
                        .setAction("😂") {}
                        .show()
                }
            }

            binding.submit.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.INVISIBLE
        }

        binding.name.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.name.error = null
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.dateOfBirth.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.dateOfBirth.error = null
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.genderMenu.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.genderMenu.error = null
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun validateEntries() : Boolean {
        val error = "This field cannot be empty"

        if (binding.name.editText!!.text.toString().isEmpty()) binding.name.error = error
        if (binding.dateOfBirth.editText!!.text.toString().isEmpty()) binding.dateOfBirth.error = error
        if (binding.genderMenu.editText!!.text.toString().isEmpty()) binding.genderMenu.error = error

        return binding.name.editText!!.text.toString().isNotEmpty() &&
                binding.dateOfBirth.editText!!.text.toString().isNotEmpty() &&
                binding.genderMenu.editText!!.text.toString().isNotEmpty()
    }

}