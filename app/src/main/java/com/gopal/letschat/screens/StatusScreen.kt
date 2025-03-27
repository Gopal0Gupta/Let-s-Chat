package com.gopal.letschat.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gopal.letschat.LCViewModel

@Composable
fun StatusScreen(navController: NavController, vm : LCViewModel) {
    Text(text = "Status screen")
    BottomNavigationMenu(BottomNavigationItem.STATUSLIST,navController)
}