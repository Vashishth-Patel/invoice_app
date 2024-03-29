package com.vashishth.invoice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vashishth.invoice.R
import com.vashishth.invoice.components.*
import com.vashishth.invoice.model.btnHomeModel
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.AddBusiness.AddBusinessScreen
import com.vashishth.invoice.screens.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun hScreen(navController: NavController,viewModel: MainViewModel){
    var multiFloatingState by remember {
        mutableStateOf(MultiFloatingState.Collapsed)
    }

    val items1 = listOf(
        minFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.add_person),
            identifier = "addPerson"
        ),
        minFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.add_item),
            identifier = "addItem"
        )
    )

    Scaffold(
        bottomBar = { BottomBar(navController = navController as NavHostController)
        },
        floatingActionButton = {
            MultiFloatingButton(multiFloatingState = multiFloatingState, onMultiFabChange = {multiFloatingState = it}, items = items1,navController)
        }
    ) {
        if(viewModel.checkBusinessName() > 0) {
            hmScreen(contentPaddingValues = it, navController, hiltViewModel())
        }else{
            AddBusinessScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController,viewModel : MainViewModel = hiltViewModel()) {
    if(viewModel.checkBusinessName() > 0) {
        hScreen(navController = navController,viewModel)
    }else{
        AddBusinessScreen(navController)
    }
}

@Composable
fun hmScreen(contentPaddingValues: PaddingValues,navController: NavController,viewModel: MainViewModel){

    var btns = listOf(btnHomeModel("Customers","your customers"),
        btnHomeModel("Items","items in stock"), btnHomeModel
    ("Sales","this week's sale"),btnHomeModel
    ("Invoices","list of invoices"))

    Surface(Modifier.padding(contentPaddingValues).fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 30.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(btns) {
                    TextCard(title = it.title, modifier = Modifier.padding(5.dp), description = it.descreption) {
                        if (it.title == "Customers") {
                            navController.navigate(route = Screen.customerListScreen.route)
                        } else if (it.title == "Items") {
                            navController.navigate(route = Screen.itemListScreen.route)
                        } else if (it.title == "Sales") {
                            navController.navigate(route = Screen.salesScreen.route)
                        } else {
                            navController.navigate(route = Screen.invoicesScreen.route)
                        }
                    }
                }
            }
            businessDetailCard(navController = navController, viewModel = viewModel)
            AddBtn(modifier = Modifier.fillMaxWidth(0.7f),
                fontSize = 20.sp,
                elevation = 5.dp,
                shape = RoundedCornerShape(18.dp),
                onClick = { navController.navigate(Screen.GenerateInvoiceScreen.route) },
                title = "Generate Invoice")
        }
    }
}
