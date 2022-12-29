package com.vashishth.invoice.screens

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.vashishth.invoice.components.AddBtnIcon
import com.vashishth.invoice.data.entity.Product
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(navController: NavController,itemregNo : String?,viewModel: MainViewModel = hiltViewModel()) {
    val item = viewModel.productList.collectAsState().value.filter {
        it.itemName.uppercase() == itemregNo?.uppercase()
    }

    val sales = viewModel.invoiceItems.collectAsState().value.filter {
        it.itemName.uppercase() == item[0].itemName.uppercase()
    }

    val invoice = viewModel.invoice.collectAsState().value.filter { invoice ->
        sales.any {
            it.invoiceNumber == invoice.invoice.invoiceNumber
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { androidx.compose.material3.Text(text = if(item.isEmpty()){""}else{item[0].itemName.uppercase()}) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                })
        }
    ) {

        if (item.isEmpty()) {
            Text(text = "No item found")
        } else {
            if (itemregNo != null) {
                ItemDetailView(it,navController, product = item[0], invoice, itemName = itemregNo)
            }
        }
    }

}

@Composable
fun ItemDetailView(paddingValues: PaddingValues, navController: NavController, product: Product, invoice: List<InvoiceWithInvoiceItems>, itemName: String) {
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues)
            .background(Color.White)) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            item {
                ItemDV(title = "Sale Price", value = "$currency${product.price}")
            }
            item {
                ItemDV(title = "Purchase Price", value = "$currency${product.purchasePrice}")
            }
            item {
                ItemDV(title = "Stock Value", value = "$currency${product.price * product.stock}")
            }
            item {
                ItemDV(title = "Stock", value = "${product.stock}")
            }
        }
        TabItemDV( navController,product = product, invoice = invoice , itemName = itemName)
    }
}

@Composable
fun SalesTab(product: Product, invoice: List<InvoiceWithInvoiceItems>, itemName: String){
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()) {
        Surface(
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp), horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "SALES HISTORY", fontSize = 20.sp)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Date", fontSize = 17.sp)
            }
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Quantity", fontSize = 17.sp)
            }
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Total", fontSize = 17.sp)
            }
        }
        LazyColumn {
            item {

            }
            items(invoice) {
                SalesView(invoice = it, itemName = itemName, product)
            }
        }
    }

}

@SuppressLint("SimpleDateFormat")
@Composable
fun SalesView(invoice: InvoiceWithInvoiceItems, itemName : String, product: Product){
    val df = SimpleDateFormat("dd/mm/yy")
    val quantity = invoice.invoiceItems.filter { it.itemName.uppercase() ==  itemName.uppercase()}[0].quantity
    val price = invoice.invoiceItems.filter { it.itemName.uppercase() ==  itemName.uppercase()}[0].unitPrice
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)

    androidx.compose.material3.Surface {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
            tonalElevation = 10.dp,
            shape = RoundedCornerShape(10.dp)
            ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Column(modifier = Modifier.padding(5.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(df.format(invoice.invoice.entryDate).toString())
                }
                Column(modifier = Modifier.padding(5.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = "$quantity ${getUnitName( product.unit)}")
                }
                Column(modifier = Modifier.padding(5.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Text("$currency${quantity * price}")
                }

            }
        }
    }
}

fun getUnitName(unit : String):String{
    return unit.substring(0,unit.indexOf("(")).uppercase()
}


@Composable
fun ItemDV(
    title : String,
    value : String,
    modifier : Modifier = Modifier
){

    Surface(
        modifier = modifier.padding(5.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            androidx.compose.material3.Text(
                text = title,
                textAlign = TextAlign.Center,
                color = darkblue40,
                fontSize = 20.sp
            )
            androidx.compose.material3.Text(
                text = value,
                textAlign = TextAlign.Center,
                color = darkblue30,
                fontSize = 17.sp
            )
        }
    }

}



@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabItemDV(navController: NavController, product: Product, invoice: List<InvoiceWithInvoiceItems>, itemName: String){
    val tabItems = listOf("Slaes", "Edit")
    val pagerState = rememberPagerState(2)
    val coroutineScope = rememberCoroutineScope()
    // A surface container using the 'background' color from the theme
    androidx.compose.material3.Surface {
        Column(Modifier.padding(5.dp)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .padding(all = 20.dp)
                    .background(color = Color.Transparent)
                    .clip(RoundedCornerShape(30.dp)),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .background(color = Color.Transparent)
                            .pagerTabIndicatorOffset(
                                pagerState, tabPositions
                            )
                            .width(0.dp)
                            .height(0.dp)
                    )
                }
            ) {
                tabItems.forEachIndexed { index, title ->
                    val color = remember {
                        Animatable(Color.Transparent)
                    }

                    LaunchedEffect(
                        pagerState.currentPage == index
                    ) {
                        color.animateTo(if (pagerState.currentPage == index) blueBtn else Color.Transparent)
                    }
                    Tab(
                        text = {
                            androidx.compose.material3.Text(
                                title,
                                style = if (pagerState.currentPage == index) TextStyle(
                                    color = darkblue50,
                                    fontSize = 18.sp
                                ) else TextStyle(color = darkblue40, fontSize = 16.sp)
                            )
                        },
                        selected = pagerState.currentPage == index,
                        modifier = Modifier.background(
                            color = color.value,
                            shape = RoundedCornerShape(30.dp)
                        ),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
            HorizontalPager(
                count = tabItems.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .background(color = Color.White)
            ) { page ->
                if (page == 0) {
                    SalesTab(product = product, invoice = invoice, itemName = itemName)
                } else {
                    ItemEditScreen(navController,item = product)
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemEditScreen(navController: NavController, item:Product, viewModel: MainViewModel= hiltViewModel()){
    val keyboardController = LocalSoftwareKeyboardController.current

    val itemName = remember{
        mutableStateOf(item.itemName)
    }
    val price = remember{
        mutableStateOf("${item.price}")
    }
    val unit = remember{
        mutableStateOf(item.unit)
    }
    val hsnNumber = remember{
        if (item.hsnNumber.toString() == "null"){
            mutableStateOf("")
        }else {
            mutableStateOf("${item.hsnNumber}")
        }
    }
    val stock = remember{
        mutableStateOf("${item.stock}")
    }
    val GSTRate = remember{
        if (item.GSTRate.isNullOrBlank()){
            mutableStateOf("")
        }else{
            mutableStateOf("${item.GSTRate}")
        }
    }
    val purchasePrice = remember{
        mutableStateOf("${item.purchasePrice}")
    }
    val description = remember{
        if (item.description.isNullOrBlank()){
            mutableStateOf("")
        }else{
            mutableStateOf("${item.description}")
        }
    }

        androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.fillMaxHeight(0.8f),
                    verticalArrangement = Arrangement.Top) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.fillMaxWidth(0.6f)) {
                            androidx.compose.material3.Text(
                                text = "Item Name", modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                            )
                            OutlinedTextField(
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = itemName.value,
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    itemName.value = it
                                },
                                label = { androidx.compose.material3.Text(text = "") },
                                placeholder = { androidx.compose.material3.Text(text = "Enter Item Name") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (itemName.value.isBlank()) {
                                androidx.compose.material3.Text(
                                    text = "This field can't be blank",
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                        //TODO
                        Column(Modifier.padding(start = 8.dp)) {
                            androidx.compose.material3.Text(
                                text = "Unit", modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                            )
                            OutlinedTextField(
                                singleLine = true,
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = unit.value,
                                onValueChange = {
                                    unit.value = it
                                },
                                label = { androidx.compose.material3.Text(text = "") },
                                placeholder = { androidx.compose.material3.Text(text = "Enter Item Unit") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (unit.value.isBlank()) {
                                androidx.compose.material3.Text(
                                    text = "This field can't be blank",
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.fillMaxWidth(0.5f)) {
                            androidx.compose.material3.Text(
                                text = "Price", modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = price.value,
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    price.value = it

                                },
                                label = { androidx.compose.material3.Text(text = "") },
                                placeholder = { androidx.compose.material3.Text(text = "Enter Item Price") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (price.value.isBlank()) {
                                androidx.compose.material3.Text(
                                    text = "This field can't be blank",
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                        Column(Modifier.padding(start = 5.dp)) {
                            androidx.compose.material3.Text(
                                text = "Stock", modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = stock.value,
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    stock.value = it
                                },
                                label = { androidx.compose.material3.Text(text = "") },
                                placeholder = { androidx.compose.material3.Text(text = "Enter Item Stock") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (stock.value.isBlank()) {
                                androidx.compose.material3.Text(
                                    text = "This field can't be blank",
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                    Column {
                        androidx.compose.material3.Text(
                            text = "Purchase Price", modifier = Modifier
                                .padding(start = 4.dp)
                                .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                        )
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            value = purchasePrice.value,
                            //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                            onValueChange = {
                                purchasePrice.value = it
                            },
                            label = { androidx.compose.material3.Text(text = "") },
                            placeholder = { androidx.compose.material3.Text(text = "Enter Purchase Price") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                        if (purchasePrice.value.isBlank()) {
                            androidx.compose.material3.Text(
                                text = "This field can't be blank",
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.fillMaxWidth(0.5f)) {
                            androidx.compose.material3.Text(
                                text = "GST Rate", modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = GSTRate.value,
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    GSTRate.value = it

                                },
                                label = { androidx.compose.material3.Text(text = "") },
                                placeholder = { androidx.compose.material3.Text(text = "Enter GSTRate") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                        Column(Modifier.padding(start = 5.dp)) {
                            androidx.compose.material3.Text(
                                text = "Hsn Number", modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f), color = Color.Black, fontSize = 13.sp
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = hsnNumber.value,
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    hsnNumber.value = it
                                },
                                label = { androidx.compose.material3.Text(text = "") },
                                placeholder = { androidx.compose.material3.Text(text = "Enter HSN Number") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxHeight(1f)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    AddBtnIcon(shape = RoundedCornerShape(10.dp),
                        icon = Icons.Default.Upload,
                        backGroundColor = mattGreen,
                        tint = Color.White,
                        onClick = {
                            viewModel.updateItem(itemName = itemName.value,
                                price = price.value.toDouble(),
                                unit = unit.value,
                                hsnNumber = if(hsnNumber.value == "null" || hsnNumber.value == ""){null}else{hsnNumber.value.toInt()},
                                stock = stock.value.toInt(),
                                GSTRate = GSTRate.value.ifBlank { null },
                                purchasePrice = purchasePrice.value.toDouble(),
                                description = description.value.ifBlank { null },
                                itemNumber = item.itemNumber)
                        }, title = "Update")
                }
            }
        }
    }
