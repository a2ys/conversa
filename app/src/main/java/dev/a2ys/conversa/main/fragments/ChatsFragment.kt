package dev.a2ys.conversa.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2ys.conversa.databinding.FragmentChatsBinding
import dev.a2ys.conversa.models.Chat
import dev.a2ys.conversa.utils.ChatAdapter

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        val recyclerView: RecyclerView = binding.recyclerView
        val chatList = mutableListOf<Chat>()
        val adapter = ChatAdapter(chatList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val newChat1 = Chat("ABC", "Hello")
        val newChat2 = Chat("XYZ", "Hi")

        chatList.add(newChat1)
        chatList.add(newChat2)

        adapter.notifyItemInserted(chatList.size - 1)

        return binding.root
    }
}