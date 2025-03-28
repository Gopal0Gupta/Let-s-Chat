package com.gopal.letschat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gopal.letschat.CheckSignIn
import com.gopal.letschat.DestinationScreen
import com.gopal.letschat.LCViewModel
import com.gopal.letschat.R
import com.gopal.letschat.commonProgressBar
import com.gopal.letschat.navigateTo

@Composable
fun LoginScreen(navController: NavController, vm : LCViewModel) {
    CheckSignIn(vm = vm, navcontroller = navController)
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .verticalScroll(
                rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val context = LocalContext.current
            var emailState by remember{
                mutableStateOf(TextFieldValue())
            }
            var passwordState by remember{
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current
            Image(painter =
            painterResource(
                id = R.drawable.clogo),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Sign In",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = emailState,
                onValueChange = {
                    emailState = it
                },
                label = {Text(text = "Email")},
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = {
                    passwordState = it
                },
                label = {Text(text = "Password")},
                modifier = Modifier.padding(8.dp)
            )
            Button(
                onClick = {
                    vm.logIn(emailState.text,passwordState.text)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF24a0ed), // Background color
                    contentColor = Color.White // Text color
                ),
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = "LOG IN",fontWeight = FontWeight.Bold)
            }
            Text(
                text = "New User? Create an Account",
                color = Color(0xFF2B48EA),
                modifier = Modifier
                    .padding(3.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp.routes)
                    }
            )
        }
    }
    if (vm.inProcess){
        commonProgressBar()
    }
}