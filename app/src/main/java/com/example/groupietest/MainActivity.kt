package com.example.groupietest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.groupietest.databinding.ActivityMainBinding
import com.example.groupietest.databinding.ItemReceiveBinding
import com.example.groupietest.databinding.ItemSendMsgBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.databinding.BindableItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val messageAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recChat.adapter = messageAdapter
        populateData()
        binding.btnSend.setOnClickListener {
            val message = Message(
                setences = binding.etChat.text.toString(),
                sender = "me"
            )
            val sendMessageItem = SendMessageItem(message)
            messageAdapter.add(sendMessageItem)
            binding.etChat.text.clear()
            receiveAutoResponse()
        }
    }
    private fun populateData() {
        val data = listOf<Message>()
        data.forEach {
            if (it.sender == "me") {
                messageAdapter.add(SendMessageItem(it))
            } else {
                messageAdapter.add(ReceiveMessageItem(it))
            }
        }
    }

    private fun receiveAutoResponse() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            val receive = Message(
                setences = "test",
                sender = "me"
            )
            val receiveItem = ReceiveMessageItem(receive)

            messageAdapter.add(receiveItem)
        }
    }
}

class SendMessageItem(private val message: Message) : BindableItem<ItemSendMsgBinding>() {
    override fun getLayout(): Int {
        return R.layout.item_send_msg
    }

    override fun bind(viewBinding: ItemSendMsgBinding, position: Int) {
        Log.d("message",message.toString())
        viewBinding.message = message
    }
}

class ReceiveMessageItem(private val message: Message) : BindableItem<ItemReceiveBinding>() {
    override fun getLayout(): Int {
        return R.layout.item_receive
    }

    override fun bind(viewBinding: ItemReceiveBinding, position: Int) {
        viewBinding.message = message
    }
}