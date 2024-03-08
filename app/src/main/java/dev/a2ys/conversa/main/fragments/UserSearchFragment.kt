package dev.a2ys.conversa.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.a2ys.conversa.R
import dev.a2ys.conversa.databinding.FragmentUserSearchBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.a2ys.conversa.models.User
import dev.a2ys.conversa.utils.UserAdapter


class UserSearchFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentUserSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSearchBinding.inflate(layoutInflater, container, false)

        databaseReference = FirebaseDatabase.getInstance().reference.child("registeredUsers")

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        userAdapter = UserAdapter()
        recyclerView.adapter = userAdapter

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUsers(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        requireActivity().onBackPressedDispatcher.addCallback {
            if (binding.searchView.isShowing) {
                binding.searchView.hide()
            } else {
                Navigation.findNavController(requireActivity(), R.id.main_navigation)
                    .navigate(R.id.action_userSearchFragment_to_chatsFragment)
            }
        }

        return binding.root
    }

    private fun searchUsers(searchText: String) {
        if (searchText.isEmpty()) {
            userAdapter.submitList(emptyList())
            return
        }

        val query = databaseReference.orderByChild("username")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .limitToFirst(3)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }
                userList.sortBy { it.username.length }

                if (userList.isNotEmpty()) {
                    userAdapter.submitList(userList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showError("Failed to fetch users: ${error.message}")
            }
        })
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Got it") {}
            .show()
    }
}