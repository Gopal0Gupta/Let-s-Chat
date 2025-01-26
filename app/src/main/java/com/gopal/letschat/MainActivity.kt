package com.gopal.letschat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gopal.letschat.screens.ChatListScreen
import com.gopal.letschat.screens.LoginScreen
import com.gopal.letschat.screens.SignUpScreen
import com.gopal.letschat.ui.theme.LetsChatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var routes : String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}"){
        fun createRoute(Id : String) = "singleChat/$Id"
    }

    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}"){
        fun createRoute(userId : String) = "singleStatus/$userId"
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LetsChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {
        val nav_controller = rememberNavController()
        val vm = hiltViewModel<LCViewModel>()

        NavHost(navController = nav_controller, startDestination = DestinationScreen.SignUp.routes){
            composable(DestinationScreen.SignUp.routes){
                SignUpScreen(nav_controller,vm)
            }
            composable(DestinationScreen.Login.routes){
                LoginScreen()
            }
            composable(DestinationScreen.ChatList.routes){
                ChatListScreen()
            }
        }
    }
}
