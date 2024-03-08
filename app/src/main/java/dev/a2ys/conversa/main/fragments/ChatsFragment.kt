package dev.a2ys.conversa.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.a2ys.conversa.R
import dev.a2ys.conversa.databinding.FragmentChatsBinding
import com.google.android.material.textview.MaterialTextView
import dev.a2ys.conversa.models.Chat
import dev.a2ys.conversa.utils.ChatAdapter

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var illustration: ImageView
    private lateinit var caption: MaterialTextView
    private lateinit var chatList: MutableList<Chat>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        recyclerView = binding.recyclerView
        illustration = binding.searchIllustration
        caption = binding.caption

        chatList = mutableListOf()
        val adapter = ChatAdapter(chatList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        updateUI()

        binding.extendedFab.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.main_navigation)
                .navigate(R.id.action_chatsFragment_to_userSearchFragment)
        }

//        val newChat1 = Chat("ABC", "Hello")
//        val newChat2 = Chat("XYZ", "Hi")
//
//        chatList.add(newChat1)
//        chatList.add(newChat2)
//
//        updateUI()
//        adapter.notifyItemInserted(chatList.size - 1)

        return binding.root
    }

    private fun updateUI() {
        if (chatList.size == 0) {
            recyclerView.visibility = View.GONE
            illustration.visibility = View.VISIBLE
            caption.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            illustration.visibility = View.GONE
            caption.visibility = View.GONE
        }
    }
}