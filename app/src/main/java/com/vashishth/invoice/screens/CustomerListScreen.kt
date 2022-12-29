package com.vashishth.invoice.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.components.customerItemView
import androidx.navigation.NavHostController
import com.vashishth.invoice.components.BottomBar
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CustomerListScreen(navController: NavController,viewModel : MainViewModel = hiltViewModel()) {
    val textState = remember { mutableStateOf("") }
    val searchedText = textState.value
    val customerList = viewModel.customerList.collectAsState().value

    Scaffold(
        bottomBar = { BottomBar(navController = navController as NavHostController)
        },
        topBar = {
            CenterAlignedTopAppBar(title = { SearchView(state = textState, placeholder = "Search Customer") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.addCustomerScreen.route)}) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon"
                        )
                    }
                })
        }
    ) {

        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
           if(customerList.isEmpty()){
               Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                   Text(text = "You have no customers yet", color = Color.Red)
               }
           }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = customerList.filter {
                    it.name.contains(searchedText,ignoreCase = true) ||
                            it.customerRegNo.toString().contains(searchedText)
                }) {
                    customerItemView( customer = it) {regNo ->
                        navController.navigate(route = Screen.customerDetailScreen.route+ "/$regNo")
                    }
                }
            }
        }
    }
}




