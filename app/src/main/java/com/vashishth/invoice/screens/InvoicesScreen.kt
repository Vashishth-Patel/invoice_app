package com.vashishth.invoice.screens

import android.R.color
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.data.entity.Customer
import com.vashishth.invoice.data.entity.Invoice
import com.vashishth.invoice.data.entity.InvoiceItem
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.blueBtn
import com.vashishth.invoice.ui.theme.darkblue50
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InvoicesScreen(modifier: Modifier=Modifier,navController: NavController, viewModel: MainViewModel = hiltViewModel()){
    var invoices = viewModel.invoice.collectAsState().value
    var customers = viewModel.customerList.collectAsState().value
    var sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    var scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    var coroutineScope = rememberCoroutineScope()


    var invoice1 = remember{
        mutableStateOf( InvoiceWithInvoiceItems(Invoice(1, Date.from(Instant.now())),List(1){
        InvoiceItem(0,1,"Milk",10,20.0)}))
    }

    var customers1 = remember{
        mutableStateOf(Customer("vashishth",9427612152,null))
    }
    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContent = {
                       CustomDialog(invoice = invoice1.value, customer = customers1.value, sheetState = sheetState)
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Invoices") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { navController.popBackStack() }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) {
        Surface(
            modifier
                .fillMaxSize()
                .padding(it)) {
            if (invoices.isEmpty()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "You have no invoices yet", color = Color.Red)
                }
            } else {
                LazyColumn {
                    items(invoices) { invoice ->
                        InvoiceView(
                            invoice = invoice,
                            customer = customers.filter { customer ->
                                customer.customerRegNo == invoice.invoice.customerRegNo }[0],
                            modifier = modifier
                        ) {bill ->
                            invoice1.value = bill
                            customers1.value =
                                customers.filter { customer ->
                                    customer.customerRegNo == invoice.invoice.customerRegNo }[0]
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

@Composable
fun InvoiceView(invoice: InvoiceWithInvoiceItems,
                customer: Customer,
                modifier: Modifier,
                onClick: (InvoiceWithInvoiceItems) -> Unit
){
    val df = SimpleDateFormat("dd-mm-yy")
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)
    var total = 0.0
    invoice.invoiceItems.forEach { total += (it.quantity*it.unitPrice)}
    androidx.compose.material3.Surface(modifier = modifier
        .clickable { onClick(invoice) }
        .padding(top = 5.dp, bottom = 5.dp, start = 22.dp, end = 22.dp)
        .fillMaxWidth(),
        shadowElevation = 7.dp,
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier.fillMaxWidth(0.6f),horizontalArrangement = Arrangement.Start) {
                Column(modifier.fillMaxWidth(0.31f)) {
                    androidx.compose.material3.Surface(
                        modifier = Modifier
                            .padding(15.dp),
                        shape = CircleShape,
                        color = Color.LightGray
                    ) {
                        Column(
                            modifier = modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = modifier
                                    .background(Color.LightGray, RoundedCornerShape(10.dp))
                                    .padding(2.dp)
                                    .size(35.dp),
                                text = customer.name.first().toString().uppercase(),
                                textAlign = TextAlign.Center,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
                Column(
                    modifier
                        .padding(start = 0.dp, end = 5.dp)
                ) {
                    Spacer(modifier.height(8.dp))
                    Text(
                        customer.name.uppercase(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = darkblue50
                    )
                    Spacer(modifier.height(10.dp))
                    Text(
                        df.format(invoice.invoice.entryDate).toString(),
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        color = darkblue50
                    )
                    Spacer(modifier.height(20.dp))

                }
            }
            Column(modifier.fillMaxWidth(0.5f)) {
                Text(text = "$currency${total}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = darkblue50)
            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomDialog(invoice: InvoiceWithInvoiceItems, customer: Customer,sheetState: BottomSheetState) {

    val df = SimpleDateFormat("dd-mm-yy")
    val date = df.format(invoice.invoice.entryDate).toString()
    var total = 0.0
    invoice.invoiceItems.forEach { total += (it.quantity*it.unitPrice)}
    val coroutineScope = rememberCoroutineScope()
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)
        androidx.compose.material3.Surface(Modifier.height(600.dp),
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            color = Color.White,
            tonalElevation = 38.dp,
            contentColor = Color.Black
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                    Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Invoice No: ${invoice.invoice.invoiceNumber}",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Text(text = date,
                                style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal
                            ))
                        }
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = colorResource(color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        sheetState.collapse()
                                    }
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${customer.name.uppercase()}\n${
                            if (customer.phoneNumber.toString().isNotBlank()) {
                                customer.phoneNumber
                            } else {
                                ""
                            }
                        }",
                        style = TextStyle(
                                fontSize = 15.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold
                    )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn {
                        item {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(modifier = Modifier.fillMaxWidth(0.3f),
                                    text = "Item",
                                    style = TextStyle(
                                        fontSize=18.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.SemiBold
                                ))
                                Text(modifier = Modifier.fillMaxWidth(0.3f),
                                    text = "Quantity",
                                    style = TextStyle(
                                        fontSize=18.sp,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.SemiBold
                                    ))
                                Text(modifier = Modifier.fillMaxWidth(0.4f),
                                    text = "Price",
                                    style = TextStyle(
                                        fontSize=18.sp,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.SemiBold
                                    ))
                            }
                        }
                        items(invoice.invoiceItems) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(modifier = Modifier.fillMaxWidth(0.38f),
                                    text =it.itemName)
                                Text(modifier = Modifier.fillMaxWidth(0.22f),
                                    text =it.quantity.toString())
                                Text(modifier = Modifier.fillMaxWidth(0.4f),
                                    text = currency + it.unitPrice.toString())
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    Divider(color = Color.Black, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(5.dp))


                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total",
                            style = TextStyle(
                                fontSize=18.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        ))
                        Text(
                            currency + total.toString(),
                            style = TextStyle(
                                fontSize=18.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        ))
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        AddBtn(borderStroke = BorderStroke(0.dp, blueBtn),
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.collapse()
                                }
                            },
                            title = "DONE",
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }
}