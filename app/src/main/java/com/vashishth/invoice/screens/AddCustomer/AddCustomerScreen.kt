package com.vashishth.invoice.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.screens.AddCustomer.AllFormEvent
import com.vashishth.invoice.screens.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerScreen(navController: NavController){
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Add Customer") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Localized description"
                    )
                }
            }
        )
    }
    ){
        addScreen(contentPaddingValues = it, navController = navController, viewModel = hiltViewModel())
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun addScreen(contentPaddingValues: PaddingValues,navController: NavController,viewModel: MainViewModel){
    val state = viewModel.state
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    Surface(Modifier.padding(contentPaddingValues)) {
        LaunchedEffect(key1 = context){
            viewModel.validationEvents.collect{event ->
                when(event){
                    is MainViewModel.ValidationEvent.Success ->{
                        Toast.makeText(
                            context,
                            "CUSTOMER ADDED",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

                Surface(modifier = Modifier.padding(0.dp),shape = RoundedCornerShape(corner = CornerSize(10.dp)), shadowElevation = 10.dp) {
                    Column(
                        Modifier
                            .height(300.dp)
                            .padding(10.dp),verticalArrangement = Arrangement.SpaceEvenly) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = state.name,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = "Name Icon"
                            )
                        },
                        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                        onValueChange = {
                            viewModel.onEvent(AllFormEvent.nameChanged(it))
                        },
                        label = { Text(text = "Customer Name") },
                        placeholder = { Text(text = "Enter Customer Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
                    )
                        if (state.nameError != null) {
                            Text(
                                text = state.nameError,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = if(state.phone != null){state.phone}else{""},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "CAll Icon"
                            )
                        },
                        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                        onValueChange = {
                            viewModel.onEvent(AllFormEvent.phoneChanged(it))
                        },
                        label = { Text(text = "Customer Phone") },
                        placeholder = { Text(text = "Enter Customer Phonenumber") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
                    )
                        if (state.phoneError != null) {
                            Text(
                                text = state.phoneError,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = if(state.email != null){state.email}else{""},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon"
                            )
                        },
                        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                        onValueChange = {
                            viewModel.onEvent(AllFormEvent.emailChanged(it))
                        },
                        label = { Text(text = "Customer Email") },
                        placeholder = { Text(text = "Enter Customer Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions { keyboardController?.hide() }
                    )
                        if (state.emailError != null) {
                            Text(
                                text = state.emailError,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                }

            }
            AddBtn(onClick = {
                viewModel.onEvent(AllFormEvent.CustomerSubmit)
            }, title = "Save")
        }
    }
}