package com.gopal.letschat.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gopal.letschat.LCViewModel

@Composable
fun SingleChatScreen(navController: NavController, vm : LCViewModel, chatId : String) {
    Text(text = "Single Chat screen")
}