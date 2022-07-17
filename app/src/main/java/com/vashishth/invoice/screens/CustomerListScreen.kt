package com.vashishth.invoice.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.components.customerItemView
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.vashishth.invoice.components.BottomBar
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(navController: NavController) {

    Scaffold(
        bottomBar = { BottomBar(navController = navController as NavHostController)
        },
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Customers") },
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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon")
                    }
                })
        }
    ) {
        clScreen(navController = navController, viewModel = hiltViewModel(),it)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun clScreen(navController: NavController,viewModel: MainViewModel,contentPaddingValues: PaddingValues){
    val customerList = viewModel.customerList.collectAsState().value
    Surface(
        Modifier
            .fillMaxSize()
            .padding(contentPaddingValues)) {
        LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = contentPaddingValues) {
            items(customerList) {
//                customerItemView(regNo = it.customerRegNo, name = it.name, phone = it.phoneNumber) {
//                    //TODO onclick
//                }

                customerItemView(regNo = it.customerRegNo, name = it.name, phone = it.phoneNumber, customer = it) {
                    viewModel.deleteCustomer(it)
                }
            }
        }
    }

}





