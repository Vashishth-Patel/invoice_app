package com.vashishth.invoice.screens.AddItem

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.data.entity.Product
import com.vashishth.invoice.data.relations.CartAndProduct
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.AddCustomer.cartItemFormEvent
import com.vashishth.invoice.screens.SearchView
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.darkblue30
import com.vashishth.invoice.ui.theme.darkblue50
import java.util.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddInvoiceItemScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val textState = remember { mutableStateOf("") }
    val searchedText = textState.value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { SearchView(state = textState, placeholder = "Search Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.addItemScreen.route)}) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon"
                        )
                    }
                })
        }
    ){
        val itemList = viewModel.productList.collectAsState().value.filter {
            it.stock >  0
        }
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it),
        contentColor = Color.Black) {
            Column(

            ) {
                if (itemList.isEmpty()){
                    Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                        Text(text = "Your shop's Stock is empty", color = Color.Red)
                    }

                }
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)) {
                    items(items = itemList.filter { item ->
                        item.itemName.contains(searchedText, ignoreCase = true) ||
                                item.description!!.contains(searchedText, ignoreCase = true) ||
                                item.itemNumber.toString().contains(searchedText, ignoreCase = true)
                    }) {
                        invoiceItemView(item = it) {
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
//                    TODO add counter functionality
                    Column() {
                        Text(text = "TOTAL(${viewModel.cartAndProductItemsList.collectAsState().value.filter { it.cart.quantity > 0 }.size}) " )
                        Text(text = "₹ ${calculateTotal(viewModel.cartAndProductItemsList.collectAsState().value)}")
                    }

                    Surface(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth(0.5f),
                        tonalElevation = 100.dp,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, darkblue30),
                        onClick = {
                            navController.navigate(Screen.GenerateInvoiceScreen.route)
                        }) {
                        Row(
                            modifier = Modifier
                                .padding(top = 15.dp, bottom = 15.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            androidx.compose.material.Text(text = "ADD TO INVOICE", color = darkblue30)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun invoiceItemView(
    item : Product,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onClick : () -> Unit
){
    var context = LocalContext.current

    Surface(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {
            onClick.invoke()
        },
        shape = RoundedCornerShape(5.dp),
        tonalElevation = 16.dp,
        contentColor = darkblue50

    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = modifier.fillMaxWidth(0.6f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = modifier
                            .background(Color.LightGray, RoundedCornerShape(10.dp))
                            .padding(2.dp)
                            .size(35.dp),
                        text = item.itemName.first().toString().uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = item.itemName.replaceFirstChar {if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()) else it.toString() }
                    )
                }
                Row(modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                    Column() {
                        Text(modifier = modifier.alpha(0.7f),
                            fontSize = 14.sp,
                            text = "₹ ${item.price} / ${item.unit}",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light)
                        Text(text = "Available: ${item.stock}",
                            modifier = modifier.alpha(0.7f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light)
                    }
                }
            }
            Column(modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                val counter: MutableState<Int> = remember{mutableStateOf(-1)}
                if (counter.value < 0 && item.stock >= 1){
                    androidx.compose.material.Surface(
                        modifier = modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, darkblue30),
                        onClick = {
                            counter.value++
                            if (counter.value <= item.stock) {
                                addItemToCart(
                                    item = item.itemName,
                                    counter = counter.value.toString(),
                                    viewModel = viewModel
                                )
                            }else{
                                counter.value--
                                //TODO TOAST
                                Toast.makeText(context,"Not enough stock",Toast.LENGTH_SHORT).show()
                                counter.value = 0
                                viewModel.updateCartItem(
                                    name = item.itemName,
                                    counter.value.toString().toInt()
                                )
                            }
                        }) {
                        Row(
                            modifier = modifier
                                .padding(top = 16.dp, bottom = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            androidx.compose.material.Text(text = "Add", color = darkblue30)
                        }
                    }
                } else {
                        Row(
                            modifier = modifier
                                .padding(top = 5.dp, bottom = 5.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {
                                if (counter.value >= 0) {
                                    counter.value--
                                    if(counter.value <= item.stock) {
                                        viewModel.updateCartItem(
                                            name = item.itemName,
                                            counter.value.toString().toInt()
                                        )
                                    }else{
                                        //TODO TOAST
                                        Toast.makeText(context,"Not enough stock",Toast.LENGTH_SHORT).show()
                                        counter.value = 0
                                        viewModel.updateCartItem(
                                            name = item.itemName,
                                            counter.value.toString().toInt()
                                        )
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Remove Icon"
                                )
                            }
                            OutlinedTextField(
                                    value = if (counter.value >= 1) {
                                        counter.value.toString()}
                                    else{
                                        ""
                                        },
                                    onValueChange = {
                                            if (it.isNullOrBlank()){
                                                counter.value = 0
                                            }
                                            else if (it.isDigitsOnly()){
                                                counter.value = it.toInt()
                                                if(counter.value <= item.stock) {
                                                    viewModel.updateCartItem(
                                                        name = item.itemName,
                                                        counter.value.toString().toInt()
                                                    )
                                                }else{
                                                    //TODO TOAST
                                                    Toast.makeText(context,"Not enough stock",Toast.LENGTH_SHORT).show()
                                                    counter.value = 0
                                                    viewModel.updateCartItem(
                                                        name = item.itemName,
                                                        counter.value.toString().toInt()
                                                    )
                                                }
                                            }else{
                                                counter.value = 0
                                            }

                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                    ),
                                    maxLines = 1,
                                    textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp),
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                        .background(Color.White, RoundedCornerShape(4.dp))
                                )
                            IconButton(
                                onClick = {
                                        counter.value++
                                    if (counter.value <= item.stock) {
                                        viewModel.updateCartItem(
                                            name = item.itemName,
                                            counter.value.toString().toInt()
                                        )
                                    }else{
                                        //TODO TOAST
                                        Toast.makeText(context,"Not enough stock",Toast.LENGTH_SHORT).show()
                                        counter.value  = 0
                                        viewModel.updateCartItem(
                                            name = item.itemName,
                                            counter.value.toString().toInt()
                                        )
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Icon"
                                )
                            }
                        }
                    }
                }
        }
    }
}

fun calculateTotal(itemList: List<CartAndProduct>): Double {
    var x = 0.00
    itemList.forEach {
        x = x + it.cart.quantity.toDouble() * it.product.price
    }
    return x
}

fun addItemToCart(item : String,counter:String,viewModel: MainViewModel){
    viewModel.onCartItemEvent(event = cartItemFormEvent.cartItemNameAdd(item))
    viewModel.onCartItemEvent(event = cartItemFormEvent.cartItemCount(counter))
    viewModel.onCartItemEvent(event = cartItemFormEvent.AddItemToCart)
}