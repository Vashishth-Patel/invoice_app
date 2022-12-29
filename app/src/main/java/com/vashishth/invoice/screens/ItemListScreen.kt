package com.vashishth.invoice.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vashishth.invoice.components.BottomBar
import com.vashishth.invoice.components.SimpleTextField
import com.vashishth.invoice.components.itemView
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.darkblue50


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(navController: NavController,viewModel: MainViewModel = hiltViewModel()) {
    val textState = remember { mutableStateOf("") }
    val searchedText = textState.value
    val itemList = viewModel.productList.collectAsState().value
    var animatedVisibility by remember {
        mutableStateOf(true)
    }
    val density = LocalDensity.current

    AnimatedVisibility(visible = animatedVisibility,
    enter = slideInVertically {
        // Slide in from 40 dp from the top.
        with(density) { -40.dp.roundToPx() }
    } + expandVertically(
        // Expand from the top.
        expandFrom = Alignment.Top
    ) + fadeIn(
        // Fade in with the initial alpha of 0.3f.
        initialAlpha = 0.3f
    ),
        exit = slideOutVertically()
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(navController = navController as NavHostController)
            },
            topBar = {
                CenterAlignedTopAppBar(title = {
                    SearchView(
                        state = textState,
                        placeholder = "Search Items"
                    )
                },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.addItemScreen.route) }) {
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

                Column {
                    if (itemList.isEmpty()) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(text = "You have no items in stock yet", color = Color.Red)
                        }
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(items = itemList.filter {
                            it.itemName.contains(searchedText, ignoreCase = true) ||
                                    it.description!!.contains(searchedText, ignoreCase = true) ||
                                    it.itemNumber.toString()
                                        .contains(searchedText, ignoreCase = true)
                        }) {
                            itemView(item = it) {
                                navController.navigate(route = Screen.itemDetailScreen.route + "/${it.itemName}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    placeholder: String
) {
    SimpleTextField(value = state.value,
        modifier = modifier
            .background(Color.White, RoundedCornerShape(15.dp))
            .height(38.dp)
            .border(width = 1.dp, color = Color.LightGray, RoundedCornerShape(15.dp)),
        textStyle = TextStyle(fontSize = 16.sp),
        singleLine = true,
        leadingIcon = { Icon(modifier = modifier.padding(start = 7.dp, end = 5.dp),imageVector = Icons.Filled.Search, contentDescription = "Search Icon")},
        placeholderText = placeholder,
        trailingIcon = { Icon(modifier = modifier
            .clickable { state.value = "" }
            .padding(start = 5.dp, end = 7.dp),imageVector = Icons.Default.Clear, contentDescription = "Clear Icon")},
        onValueChange = {value ->
            state.value = value
        })
}



