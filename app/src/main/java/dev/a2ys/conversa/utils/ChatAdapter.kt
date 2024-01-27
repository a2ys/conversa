package dev.a2ys.conversa.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a2ys.conversa.databinding.ChatItemBinding
import dev.a2ys.conversa.models.Chat

class ChatAdapter(private val chatList: List<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private lateinit var binding: ChatItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val data = chatList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatViewHolder(private val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.textSender.text = chat.sender
            binding.textMessage.text = chat.message
        }
    }

}