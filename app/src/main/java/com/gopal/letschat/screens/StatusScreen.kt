package com.gopal.letschat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gopal.letschat.CommonDivider
import com.gopal.letschat.DestinationScreen
import com.gopal.letschat.LCViewModel
import com.gopal.letschat.TitleText
import com.gopal.letschat.commonProgressBar
import com.gopal.letschat.commonRow
import com.gopal.letschat.navigateTo

@Composable
fun StatusScreen(navController: NavController, vm: LCViewModel) {
    val inProcess = vm.inProgressStatus
    if (inProcess) {
        commonProgressBar()
    } else {
        val statuses = vm.status
        val userData = vm.userdata
        val myStatus = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatus = statuses.filter {
            it.user.userId != userData?.userId
        }
        Scaffold(
            floatingActionButton = {
                FAB {

                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    TitleText(txt = "Status")
                    if (statuses.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No Statuses Available")
                        }
                    } else {
                        if (myStatus.isNotEmpty()) {
                            commonRow(
                                imageUrl = myStatus[0].user.imageUrl,
                                name = myStatus[0].user.name
                            ) {
                                navigateTo(
                                    navController = navController,
                                    route = DestinationScreen.SingleStatus.createRoute(myStatus[0].user.userId!!)
                                )
                            }
                            CommonDivider()
                            val uniqueUsers = otherStatus.map { it.user }.toSet().toList()
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(uniqueUsers){ users->
                                    commonRow(imageUrl = users.imageUrl, name = users.name) {
                                        navigateTo(navController,DestinationScreen.SingleStatus.createRoute(users.userId!!))
                                    }
                                }
                            }
                        }
                        BottomNavigationMenu(BottomNavigationItem.STATUSLIST, navController)
                    }
                }
            }
        )
    }
}

@Composable
fun FAB(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = "Add Status",
            tint = Color.White
        )
    }
}