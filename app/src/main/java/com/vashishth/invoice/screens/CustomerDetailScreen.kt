package com.vashishth.invoice.screens

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
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
import com.vashishth.invoice.data.entity.Customer
import com.vashishth.invoice.data.entity.Invoice
import com.vashishth.invoice.data.entity.InvoiceItem
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(navController: NavController,customerregNo : String?,viewModel: MainViewModel = hiltViewModel()) {
    val customer = viewModel.customerList.collectAsState().value.filter {
        it.customerRegNo.toString() == customerregNo
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "") },
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
        if (customer.isNotEmpty()) {
            CustomerDetailView(
                customer = customer[0],
                navController = navController,
                paddingValues = it,
                viewModel = viewModel
            )
        }
        else{
            Text(text = "No customer found")
        }
    }

}

@Composable
fun CustomerDetailView(customer: Customer, navController: NavController, paddingValues: PaddingValues, viewModel: MainViewModel){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(paddingValues)) {
            Column(modifier = Modifier
                .weight(0.25f)
                ) {
//                Surface(modifier = Modifier
//                    .fillMaxSize()
//                    .padding(10.dp),
//                    shape = RoundedCornerShape(20.dp),
//                    tonalElevation = 1.dp,
//                    shadowElevation = 5.dp
//                ) {
                    Column(modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Surface(modifier = Modifier
                            .padding(2.dp)
                            .size(100.dp),
                            shape = CircleShape,
                            color = Color.LightGray
                            ) {
                            Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    modifier = Modifier
                                        .padding(2.dp),
                                    text = customer.name.first().toString().uppercase(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 47.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Column() {
                            Text(text = customer.name.uppercase(), fontSize = 30.sp, color = darkblue50)
                        }
                        
                    }

//                }

            }
            Surface(modifier = Modifier
                .weight(0.75f),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                tonalElevation = 5.dp

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                ) {
                    Tab(navController = navController, viewModel = viewModel, customer = customer)
                }
            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tab(navController: NavController, viewModel: MainViewModel, customer: Customer){
    val tabItems = listOf("Transactions", "Details")
    val pagerState = rememberPagerState(2)
    val coroutineScope = rememberCoroutineScope()
        // A surface container using the 'background' color from the theme
        Surface() {
            Column {
                androidx.compose.material.TabRow(
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
                                Text(
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
                        .background(color = Color.Blue)
                ) { page ->
                    if(page == 0){
                        transactionTab(viewModel,customer)
                    }else{
                        detailsTab(navController = navController,viewModel,customer)
                    }
                }
            }
        }
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun transactionTab(viewModel: MainViewModel,customer: Customer){
    var invoices = viewModel.invoice.collectAsState().value.filter { it.invoice.customerRegNo == customer.customerRegNo }
    var sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    var scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    var coroutineScope = rememberCoroutineScope()
    var invoice1 = remember {
        mutableStateOf(
            InvoiceWithInvoiceItems(
                Invoice(1, Date.from(Instant.now())),
                List(1) {
                    InvoiceItem(0, 1, "Milk", 10, 20.0)
                })
        )
    }

    var customers1 = remember {
        mutableStateOf(customer)
    }

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContent = {
            CustomDialog(invoice = invoice1.value, customer = customers1.value, sheetState = sheetState)
        },
        sheetPeekHeight = 0.dp,
    ) {
        Surface(modifier = Modifier.fillMaxSize().padding(it)) {
            if (invoices.isEmpty()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "You have no invoices yet", color = Color.Red)
                }
            } else {
                LazyColumn {
                    items(invoices) { invoice ->
                        InvoiceView(
                            invoice = invoice,
                            customer = customer,
                            modifier = Modifier
                        ) { bill ->
                            invoice1.value = bill
                            customers1.value = customer
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                        }
                    }
                }
            }

        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun detailsTab(navController: NavController, viewModel: MainViewModel,customer: Customer) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var name = remember {
        mutableStateOf("${customer.name}")
    }
    var phone = remember {
        mutableStateOf(
            if (customer.phoneNumber.toString().isNullOrBlank()) {
                ""
            } else {
                "${customer.phoneNumber}"
            }
        )
    }
    var email = remember {
        mutableStateOf(if (customer.email.isNullOrBlank()) {
            ""
        } else {
            "${customer.email}"
        })
    }


    Surface(modifier = Modifier.fillMaxSize()) {
//        Text(text = "Details tab")
        Surface(Modifier.padding()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier.padding(0.dp).fillMaxHeight(0.7f),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    tonalElevation = 10.dp
                ) {
                    Column(
                        Modifier
                            .height(350.dp)
                            .padding(8.dp), verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column() {
                            Text(text = "Customer Name", modifier = Modifier
                                .padding(start = 4.dp)
                                .alpha(0.6f), color = Color.Black, fontSize = 13.sp)
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = name.value,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Name Icon"
                                    )
                                },
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    name.value = it
                                },
                                label = { Text(text = "") },
                                placeholder = { Text(text = "Enter Customer Name") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (name.value.isNullOrBlank()) {
                                Text(
                                    text = "This field can't be blank",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                        Column() {
                            Text(
                                text = "Contact Number",
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f),
                                color = Color.Black,
                                fontSize = 13.sp
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = phone.value,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "CAll Icon"
                                    )
                                },
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    phone.value = it
                                },
                                label = { Text(text = "") },
                                placeholder = { Text(text = "Enter Customer Phonenumber") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (!phone.value.isNullOrBlank()) {
                                if (phone.value.length != 10) {
                                    Text(
                                        text = "Enter valid phone number",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                            }
                        }

                        Column() {
                            Text(
                                text = "Customer Email",
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .alpha(0.6f),
                                color = Color.Black,
                                fontSize = 13.sp
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                value = email.value,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email Icon"
                                    )
                                },
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    email.value = it
                                },
                                label = { Text(text = "") },
                                placeholder = { Text(text = "Enter Customer Email") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions { keyboardController?.hide() }
                            )
                            if (!email.value.isNullOrBlank()) {
                                if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                    Text(
                                        text = "Enter valid Email",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                            }
                        }
                    }

                }
                Row(Modifier.fillMaxHeight(0.7f)) {
//                    AddBtnIcon(shape = RoundedCornerShape(10.dp),
//                        backGroundColor = mattRed,
//                        tint = Color.White,
//                        icon = Icons.Default.Delete,
//                        onClick = {
//                            viewModel.deleteCustomer(customer)
//                            navController.popBackStack()
//                        }, title = "Delete")
                    AddBtnIcon(
                        shape = RoundedCornerShape(10.dp),
                        icon = Icons.Default.Upload,
                        backGroundColor = mattGreen,
                        tint = Color.White,
                        onClick = {
                            viewModel.updateCustomer(name = name.value, phoneNumber = if(phone.value.isNullOrBlank()){null}else{phone.value.toLong()},email = if(email.value.isNullOrBlank()){null}else{email.value},id = customer.customerRegNo)
                        }, title = "Update")
                }

            }
        }
    }
}
