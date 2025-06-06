package com.gopal.letschat.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gopal.letschat.CommonDivider
import com.gopal.letschat.CommonImage
import com.gopal.letschat.LCViewModel
import com.gopal.letschat.data.Message
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, chatId: String) {
    var reply by rememberSaveable {
        mutableStateOf("")
    }
    var onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }
    var chatMessages = vm.chatMessages
    val myuser = vm.userdata
    val currentChat = vm.chats.value.first { it.chatId == chatId }
    val chatUser =
        if (myuser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1
    LaunchedEffect(key1 = Unit) {
        vm.populateMessage(chatId)
    }
    BackHandler {
        vm.dePopulateMessage()
        navController.popBackStack()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(name = chatUser.name ?: "Unknown User", imageUrl = chatUser.imageUrl ?: "") {
            navController.popBackStack()
            vm.dePopulateMessage()
        }
        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessage = chatMessages,
            currentUserId = myuser?.userId?: ""
        )
        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
    }
}

@Composable
fun MessageBox(modifier: Modifier, chatMessage: List<Message>, currentUserId: String) {
    LazyColumn(modifier = modifier) {
        items(chatMessage) { msg ->
            val alignment = if (msg.sendby == currentUserId) Alignment.End else Alignment.Start
            val backgroundColor = if (msg.sendby == currentUserId) Color(0xFF5B8148) else Color(0xFF5B5A5A)

            val formattedTime = try {
                val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                val date = parser.parse(msg.timestamp ?: "")
                if (date != null) formatter.format(date) else ""
            } catch (e: Exception) {
                ""
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = alignment
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(backgroundColor)
                        .padding(8.dp)
                ) {
                    Text(
                        text = msg.message ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formattedTime,
                        color = Color.LightGray,
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(Icons.Rounded.ArrowBack, contentDescription = null,
            Modifier
                .clickable {
                    onBackClicked.invoke()
                }
                .padding(8.dp))
        CommonImage(
            imageUrl,
            Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(text = name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Button(onClick = onSendReply) {
                Text(text = "Send")
            }
        }
    }
}