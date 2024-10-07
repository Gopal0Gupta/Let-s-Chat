package com.gopal.letschat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

fun navigateTo(navController : NavController,route : String){
    navController.navigate(route){
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun commonProgressBar(){
    Row(
        modifier = Modifier
            .alpha(0.5F)
            .background(Color.LightGray)
            .clickable(enabled = false) {}
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CheckSignIn(vm : LCViewModel,navcontroller : NavController) {
    var alreadySignIn by remember {
        mutableStateOf(false)
    }
    val signIn = vm.signIn
    if (signIn && !alreadySignIn){
        alreadySignIn = true
        navcontroller.navigate(DestinationScreen.ChatList.routes){
            popUpTo(0)
        }
    }
}
