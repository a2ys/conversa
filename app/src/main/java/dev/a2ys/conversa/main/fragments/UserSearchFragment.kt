package dev.a2ys.conversa.main.fragments

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2ys.conversa.databinding.FragmentUserSearchBinding
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

        binding.search.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUsers(s.toString())
            }
        })

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
                userAdapter.submitList(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("HANDLE DB ERROR")
            }
        })
    }
}