package com.vashishth.invoice.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.R
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.components.TextSearchBar
import com.vashishth.invoice.data.entity.Customer
import com.vashishth.invoice.data.relations.CartAndProduct
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.AddCustomer.CustomerFormEvent
import com.vashishth.invoice.screens.AddCustomer.CustomerFormState
import com.vashishth.invoice.screens.AddCustomer.invoiceFromEvent
import com.vashishth.invoice.screens.AddCustomer.invoiceItemFormEvent
import com.vashishth.invoice.screens.AddInvoice.InvoiceFormState
import com.vashishth.invoice.screens.AddItem.calculateTotal
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.*
import com.vashishth.invoice.utils.autoComplete.AutoCompleteBox
import com.vashishth.invoice.utils.autoComplete.AutoCompleteSearchBarTag
import com.vashishth.invoice.utils.sendSMS
import kotlinx.coroutines.launch
import java.util.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GenerateInvoiceScreen(navController: NavController,
                          viewModel: MainViewModel = hiltViewModel()
) {
    val invoiceFormState = viewModel.invoiceState
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)
    val context = LocalContext.current
    val nameState = viewModel.state
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val nameList  = viewModel.customerList.collectAsState().value

    val lastInvoiceNumber = if(viewModel.lastInvoiceNum.collectAsState().value.isEmpty()){1}else{viewModel.lastInvoiceNum.collectAsState().value.last()+1}

    val cartItems = viewModel.cartItemsList.collectAsState().value
    val cartAndProductItems = viewModel.cartAndProductItemsList.collectAsState().value

    var lists = StringBuilder()
    cartAndProductItems.filter {
        it.cart.quantity != 0
    }.forEach {
        lists.append(
            "${it.product.itemName} = ₹${it.product.price} x ${it.cart.quantity} ${it.product.unit}\n" +
                    "$currency${it.product.price * it.cart.quantity}").append(";\n")
    }

    var   msg = "Hi ${nameState.name},\n" +
            "Your shopping details :\n" +
            lists.removeSuffix(";").toString()+
            "Total = $currency${calculateTotal(cartAndProductItems)}\n"


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

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContent = {
            nameSheet(viewModel = viewModel, context = context, sheetState = sheetState, nameList = nameList)
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            CenterAlignedTopAppBar(title = { androidx.compose.material3.Text("Add Item") },
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
        Column(modifier = Modifier.padding(it), verticalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier.fillMaxHeight(0.85f)) {
                header(nameState = nameState,
                    viewModel = viewModel,
                    invoiceFormState = invoiceFormState,
                    context = context,
                    sheetState = sheetState,
                    lastInvoiceNumber = lastInvoiceNumber
                )

                selectedItems(cartAndProductItems)
            }
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    selectItem(
                        viewModel = viewModel,
                        invoiceFormState = invoiceFormState,
                        context = context,
                        navController = navController
                    )
                    AddBtn(modifier = Modifier
                        .padding(top = 6.dp, bottom = 6.dp)
                        .fillMaxWidth(1f),
                        borderStroke = BorderStroke(2.dp, blue30),
                        fontSize = 16.sp,
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            if (!nameState.name.isNullOrBlank() && cartAndProductItems.any{it.cart.quantity > 0}){
                            if(!nameList.any {
                                it.name.uppercase() == nameState.name.uppercase() && it.phoneNumber == nameState.phone?.toLong()
                                }){
                                viewModel.onEvent(CustomerFormEvent.CustomerSubmit)
                                if (nameList.size == 0){
                                    viewModel.onInvoiceFormEvent(invoiceFromEvent.customerRegNoChanged("1"))
                                }else{
                                    Log.d("invoice size","${nameList.size}")
                                    viewModel.onInvoiceFormEvent(invoiceFromEvent.customerRegNoChanged((nameList.size+1).toString()))
                                }
                                //

                                cartAndProductItems.filter {
                                    it.cart.quantity > 0 && it.cart.quantity <= it.product.stock
                                }.forEach {
                                    viewModel.onInvoiveItemFormEvent(
                                        invoiceItemFormEvent.invoiceNumberChanged(lastInvoiceNumber.toString())
                                    )
                                    viewModel.onInvoiveItemFormEvent(
                                        invoiceItemFormEvent.itemNameChanged(it.cart.itemName)
                                    )
                                    viewModel.onInvoiveItemFormEvent(
                                        invoiceItemFormEvent.itemCountChanged(it.cart.quantity.toString())
                                    )
                                    viewModel.onInvoiveItemFormEvent(
                                        invoiceItemFormEvent.unitPriceChanged(it.product.price.toString())
                                    )
                                    //TODO count limit
                                    viewModel.updateItemCount(it.product.stock - it.cart.quantity,it.product.itemNumber)
                                    viewModel.onInvoiveItemFormEvent(
                                        invoiceItemFormEvent.addInvoiceItem
                                    )
                                }
                                viewModel.onInvoiceFormEvent(
                                    invoiceFromEvent.entryDateChanged(invoiceFormState.entryDate)
                                )

                                viewModel.onInvoiceFormEvent(
                                    invoiceFromEvent.GenerateInvoice
                                )
                                sendSMS(nameState.phone.toString(),msg,context)
                                viewModel.deleteCart()

                            }else {
                                viewModel.onInvoiceFormEvent(
                                    invoiceFromEvent.customerRegNoChanged(
                                        nameList.filter {
                                            it.name.uppercase() == nameState.name.uppercase() && it.phoneNumber == nameState.phone?.toLong()
                                        }[0].customerRegNo.toString()
                                    )
                                )
                            cartAndProductItems.filter {
                                it.cart.quantity > 0 && it.cart.quantity <= it.product.stock
                            }.forEach {
                                viewModel.onInvoiveItemFormEvent(
                                    invoiceItemFormEvent.invoiceNumberChanged(lastInvoiceNumber.toString())
                                )
                                viewModel.onInvoiveItemFormEvent(
                                    invoiceItemFormEvent.itemNameChanged(it.cart.itemName)
                                )
                                viewModel.onInvoiveItemFormEvent(
                                    invoiceItemFormEvent.itemCountChanged(it.cart.quantity.toString())
                                )
                                viewModel.onInvoiveItemFormEvent(
                                    invoiceItemFormEvent.unitPriceChanged(it.product.price.toString())
                                )
                                //TODO count limit
                                viewModel.updateItemCount(it.product.stock - it.cart.quantity,it.product.itemNumber)
                                viewModel.onInvoiveItemFormEvent(
                                    invoiceItemFormEvent.addInvoiceItem
                                )
                            }
                            viewModel.onInvoiceFormEvent(
                                invoiceFromEvent.entryDateChanged(invoiceFormState.entryDate)
                            )

                            viewModel.onInvoiceFormEvent(
                                invoiceFromEvent.GenerateInvoice
                            )
                            sendSMS(nameState.phone.toString(),msg,context)
                            viewModel.deleteCart()
                                }
                            }else{
                                Toast.makeText(context,"Please Enter customer name Or Select items",Toast.LENGTH_SHORT).show()
                            }
                                  },
                        title ="Generate Invoice")
                }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun header(nameState:CustomerFormState,modifier: Modifier = Modifier,viewModel: MainViewModel,invoiceFormState: InvoiceFormState,context: Context,sheetState:BottomSheetState,lastInvoiceNumber : Int){

    val coroutineScope = rememberCoroutineScope()
    Surface(modifier = modifier.fillMaxWidth()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 2.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                headerItem(
                    modifier = modifier.weight(0.5f),
                    title = "Invoice N0.",
                    valuetext = "$lastInvoiceNumber"
                ) {

                }
                DatePicker2(
                    modifier = modifier.weight(0.5f),
                    viewModel = viewModel,
                    invoiceFormState = invoiceFormState,
                    mContext = context
                )
            }

            OutlinedTextField(modifier = modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 2.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                value = nameState.name,
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "person Icon"
                    )
                },
                placeholder = { androidx.compose.material3.Text(text = "Enter Customer Name") },
                label = { Text(text = "Customer Name")},
                trailingIcon = {
                    androidx.compose.material.IconButton(onClick = {
                        coroutineScope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            }else{
                                sheetState.collapse()
                            }
                        }
                    }
                    ) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                onValueChange = { viewModel.onEvent(CustomerFormEvent.nameChanged(it)) }
            )
            if (nameState.nameError != null) {
                androidx.compose.material3.Text(
                    text = nameState.nameError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            OutlinedTextField(modifier = modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 15.dp, end = 15.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "CAll Icon"
                    )
                },
                placeholder = { androidx.compose.material3.Text(text = "Enter Customer Phonenumber") },
                value = nameState.phone ?: "",
                label = { Text(text = "Customer Phone")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                onValueChange = { viewModel.onEvent(CustomerFormEvent.phoneChanged(it)) }
            )
            if (nameState.phoneError != null) {
                androidx.compose.material3.Text(
                    text = nameState.phoneError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun selectedItems(cartAndProduct: List<CartAndProduct>){
    LazyColumn(){
        items(cartAndProduct.filter {
            it.cart.quantity > 0
        }){
            cartItemView(item = it)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun selectItem(modifier: Modifier = Modifier,viewModel: MainViewModel,invoiceFormState: InvoiceFormState,context: Context,navController: NavController){
    Surface(
        modifier = modifier.padding(15.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, darkblue30),
        onClick = { navController.navigate(Screen.AddInvoiceItemScreen.route) }) {
        Row(
            modifier = modifier
                .fillMaxWidth(0.46f)
                .padding(top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = Icons.Default.Add,
                tint = darkblue30,
                contentDescription = "Add Icon"
            )
            Text(text = "Add Items", color = darkblue30)

        }
    }
}

@Composable
fun headerItem(
    modifier: Modifier = Modifier,
    title : String,
    valuetext : String,
    onClick : () -> Unit
){
    Column(modifier = modifier.clickable { onClick.invoke() },
        verticalArrangement = Arrangement.Center) {
        Text(text = title)
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = valuetext)
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Drop down Icon")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePicker2(modifier: Modifier = Modifier,
                viewModel: MainViewModel,
                invoiceFormState: InvoiceFormState,
                mContext : Context){
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            viewModel.onInvoiceFormEvent(invoiceFromEvent.entryDateChanged("$mDayOfMonth-${mMonth+1}-$mYear"))
        }, mYear, mMonth, mDay
    )
    androidx.compose.material.Surface(
        modifier = modifier.padding(1.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        onClick = { mDatePickerDialog.show() }) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${invoiceFormState.entryDate}")
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar"
            )
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun nameSheet(viewModel: MainViewModel,context: Context,sheetState : BottomSheetState,nameList : List<Customer>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    androidx.compose.material3.Surface(
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        modifier = Modifier.fillMaxHeight(),
        shadowElevation = 10.dp,
        color = bluelight1,
        border = BorderStroke(0.1.dp, Color.Gray)
    ) {

        Column(verticalArrangement = Arrangement.Top) {
            AutoCompleteBox(
                items = nameList,
                itemContent = { customer ->
                    NameAutoCompleteItem(customer)
                }
            ) {
                var value by remember { mutableStateOf("") }
                val view = LocalView.current

                onItemSelected { customer ->
                    value = customer.name
                    viewModel.onEvent(CustomerFormEvent.nameChanged(customer.name))
                    viewModel.onEvent(CustomerFormEvent.phoneChanged(customer.phoneNumber.toString()))
                    view.clearFocus()
                    coroutineScope.launch {
                        sheetState.collapse()
                    }
                }
                TextSearchBar(
                    modifier = Modifier.testTag(AutoCompleteSearchBarTag),
                    value = value,
                    label = "Search Customers",
                    onDoneActionClick = {
                        filter(value)
                        if (keyboardController != null) {
                            keyboardController.hide()
                        }
                    },
                    onClearClick = {
                        value = ""
                        view.clearFocus()
                    },
                    onFocusChanged = { focusState ->
                        isSearching = focusState.isFocused
                    },
                    onValueChanged = { query ->
                        value = query
                    }
                )
            }
        }
    }
}


@Composable
fun NameAutoCompleteItem(customer: Customer) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.material3.Surface(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            color = blueBtn,
            shadowElevation = 3.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                androidx.compose.material3.Surface(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(28.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        androidx.compose.material3.Text(
                            modifier = Modifier
                                .padding(2.dp),
                            text = customer.name.first().toString().uppercase(),
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                androidx.compose.material3.Text(
                    modifier = Modifier.padding(5.dp),
                    text = "${customer.name.uppercase()}",
                    style = androidx.compose.material.MaterialTheme.typography.h6,
                    color = darkblue50
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun cartItemView(item:CartAndProduct){
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)
    Surface(
        Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp)),
        tonalElevation = 16.dp
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.slot_padding))
                    .fillMaxWidth(0.8f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = item.product.itemName)
                    Text(text = "$currency${item.product.price * item.cart.quantity}")
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Qty x Rate")
                    Text(text = "${item.cart.quantity} ${item.product.unit} X ${item.product.price}")
                }
            }

            Column() {
                Surface(
                    modifier = Modifier
                        .padding(5.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, darkblue30),
                    onClick = {
                    }) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Edit", color = darkblue30)
                    }
                }
            }
        }
    }
}