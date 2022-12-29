package com.vashishth.invoice.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.components.DropDownSearchBar
import com.vashishth.invoice.components.TextSearchBar
import com.vashishth.invoice.model.hsnCodeItem
import com.vashishth.invoice.model.unit
import com.vashishth.invoice.screens.AddCustomer.cartItemFormEvent
import com.vashishth.invoice.screens.AddCustomer.itemFormEvent
import com.vashishth.invoice.screens.AddItem.ItemFormState
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.*
import com.vashishth.invoice.utils.autoComplete.AutoCompleteBox
import com.vashishth.invoice.utils.autoComplete.AutoCompleteSearchBarTag
import com.vashishth.invoice.utils.hsnLoad
import com.vashishth.invoice.utils.unitsLoad
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun addItemScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    var sheetnum by remember {
        mutableStateOf(0)
    }

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContent = {
            if (sheetnum == 0) {
                btmSheet1(viewModel,context,sheetState)
            } else {
                btmSheet2(viewModel,context,sheetState)
            }

        },
        sheetPeekHeight = 0.dp,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Add Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) {
        LaunchedEffect(key1 = context){
            viewModel.validationEvents.collect{event ->
                when(event){
                    is MainViewModel.ValidationEvent.Success ->{
                        Toast.makeText(
                            context,
                            "Item ADDED",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        Surface(Modifier.padding(it)) {
            val scrollState = rememberScrollState()
            val itemState = viewModel.itemState
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(0.8f).verticalScroll(scrollState), verticalArrangement = Arrangement.SpaceEvenly) {
                    Surface(
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                        shape = RoundedCornerShape(7.dp),
                        shadowElevation = 0.dp,
                        tonalElevation = 4.dp
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Details")
                            Row {
                                Column {
                                    OutlinedTextField(singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = itemState.itemName,
                                        onValueChange = {
                                            viewModel.onItemFormEvent(
                                                itemFormEvent.itemNameChanged(
                                                    it
                                                )
                                            )
                                        },
                                        label = { Text(text = "Item Name") },
                                        placeholder = { Text(text = "Enter Item Name") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (itemState.itemNameError != null) {
                                        Text(modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                                            text = itemState.itemNameError,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                            textAlign = TextAlign.End,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Column {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        value = itemState.unit,
                                        onValueChange = {
                                            viewModel.onItemFormEvent(
                                                itemFormEvent.itemUnitChanged(
                                                    it
                                                )
                                            )
                                        },
                                        label = { androidx.compose.material.Text(text = "UNIT") },
                                        textStyle = MaterialTheme.typography.subtitle1,
                                        singleLine = true,
                                        trailingIcon = {
                                            androidx.compose.material.IconButton(onClick = {
                                                coroutineScope.launch {
                                                    if (sheetState.isCollapsed) {
                                                        sheetnum = 0
                                                        sheetState.expand()
                                                    } else if (sheetnum == 0) {
                                                        sheetState.collapse()
                                                    } else if (sheetState.isExpanded && sheetnum == 1) {
                                                        sheetState.collapse()
                                                        sheetnum = 0
                                                        sheetState.expand()
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
                                        keyboardActions = KeyboardActions(onDone = { }),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done,
                                            keyboardType = KeyboardType.Text
                                        )
                                    )
                                    if (itemState.unitError != null) {
                                        Text(modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                                            text = itemState.unitError,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                            textAlign = TextAlign.End,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(10.dp),
                                value = if (itemState.hsnNumber.isNullOrBlank()) {
                                    ""
                                } else {
                                    itemState.hsnNumber
                                },
                                onValueChange = {
                                    viewModel.onItemFormEvent(itemFormEvent.itemHSNNumberChanged(it))
                                },
                                label = { androidx.compose.material.Text(text = "HSN NUMBER") },
                                textStyle = MaterialTheme.typography.subtitle1,
                                singleLine = true,
                                trailingIcon = {
                                    androidx.compose.material.IconButton(onClick = {
                                        coroutineScope.launch {
                                            if (sheetState.isCollapsed) {
                                                sheetnum = 1
                                                sheetState.expand()
                                            } else if (sheetnum == 1) {
                                                sheetState.collapse()
                                            } else if (sheetState.isExpanded && sheetnum == 0) {
                                                sheetState.collapse()
                                                sheetnum = 1
                                                sheetState.expand()
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
                                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                )
                            )
                            if (itemState.hsnNumberError != null) {
                                Text(modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                                    text = itemState.hsnNumberError,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.End,
                                    fontSize = 10.sp
                                )
                            }
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                singleLine = true,
                                value = if (itemState.description.isNullOrBlank()) {
                                    ""
                                } else {
                                    itemState.description
                                },
                                //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                onValueChange = {
                                    viewModel.onItemFormEvent(
                                        itemFormEvent.itemDescriptionChanged(
                                            it
                                        )
                                    )
                                },
                                label = { Text(text = "Item Description") },
                                placeholder = { Text(text = "Enter Item Description") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (itemState.descriptionError != null) {
                                Text(modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                                    text = itemState.descriptionError,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.End,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                    pricing(viewModel = viewModel, itemState = itemState)
                    stock(viewModel = viewModel, itemState = itemState, context = context)
                }

                Column(modifier = Modifier.weight(0.1f)) {
                        AddBtn(borderStroke = BorderStroke(0.dp, blueBtn),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(it), onClick = {
                                viewModel.onItemFormEvent(itemFormEvent.AddItem)
                                viewModel.onCartItemEvent(
                                    cartItemFormEvent.cartItemNameAdd(
                                        itemState.itemName
                                    )
                                )
                                viewModel.onCartItemEvent(cartItemFormEvent.cartItemCount("0"))
                                viewModel.onCartItemEvent(cartItemFormEvent.AddItemToCart)
                            }, title = "SAVE ITEM", shape = RoundedCornerShape(8.dp)
                        )
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun btmSheet1(viewModel: MainViewModel,context: Context,sheetState: BottomSheetState) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val unitsList: ArrayList<unit> = ArrayList()

    val coroutineScope = rememberCoroutineScope()

    Surface(
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        modifier = Modifier.height(600.dp),
        shadowElevation = 10.dp,
        color = bluelight1,
        border = BorderStroke(0.1.dp, Color.Gray)
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            AutoCompleteBox(
                items = unitsList,
                itemContent = { hsnItem ->
                    unitAutoCompleteItem(hsnItem.unit)
                }
            ) {
                var valueUnit by remember {
                    mutableStateOf("")
                }
                val viewUnit = LocalView.current

                onItemSelected { unit ->
                    valueUnit = unit.unit
                    viewModel.onItemFormEvent(itemFormEvent.itemUnitChanged(unit.unit))
                    filter(valueUnit)
                    if (keyboardController != null) {
                        keyboardController.hide()
                    }
                    viewUnit.clearFocus()
                    coroutineScope.launch {
                        sheetState.collapse()
                    }
                }
                DropDownSearchBar(
                    modifier = Modifier.testTag(AutoCompleteSearchBarTag),
                    value = valueUnit,
                    label = "Unit",
                    onDoneActionClick = {
                        filter(valueUnit)
                        viewUnit.clearFocus()
                        if (keyboardController != null) {
                            keyboardController.hide()
                        }
                    },
                    onClearClick = {
                        valueUnit = ""
                        filter(valueUnit)
                        isSearching = !isSearching
                    },
                    onFocusChanged = { focusState ->
                        isSearching = focusState.isFocused
                        coroutineScope.launch {
                            unitsLoad(context = context, unitsList = unitsList)
                        }
                    },
                    onValueChanged = { query ->
                        valueUnit = query
                        filter(valueUnit)
                    }
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun btmSheet2(viewModel: MainViewModel,context: Context,sheetState : BottomSheetState) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val hsnList: ArrayList<hsnCodeItem> = ArrayList()
    val coroutineScope = rememberCoroutineScope()
    Surface(
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        modifier = Modifier.height(600.dp),
        shadowElevation = 10.dp,
        color = bluelight1,
        border = BorderStroke(0.1.dp, Color.Gray)
    ) {

        Column(verticalArrangement = Arrangement.Top) {
            AutoCompleteBox(
                items = hsnList,
                itemContent = { hsnItem ->
                    PersonAutoCompleteItem(hsnItem)
                }
            ) {
                var value by remember { mutableStateOf("") }
                val view = LocalView.current

                onItemSelected { hsnCodeItem ->
                    value = hsnCodeItem.Code
                    viewModel.onItemFormEvent(itemFormEvent.itemHSNNumberChanged(hsnCodeItem.Code))
                    view.clearFocus()
                    coroutineScope.launch {
                        sheetState.collapse()
                    }
                }
                TextSearchBar(
                    modifier = Modifier.testTag(AutoCompleteSearchBarTag),
                    value = value,
                    label = "Search HSN CODES",
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
                        coroutineScope.launch {
                            hsnLoad(context, hsnList = hsnList)
                        }
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
fun PersonAutoCompleteItem(hsnCodeItem: hsnCodeItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            color = blueBtn,
            shadowElevation = 3.dp
        ) {
            Column() {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "HSN CODE : ${hsnCodeItem.Code}",
                    style = MaterialTheme.typography.h6,
                    color = darkblue50
                )
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "- ${hsnCodeItem.Description}",
                    style = MaterialTheme.typography.subtitle2,
                    color = darkblue40
                )
            }
        }
    }
}

@Composable
fun unitAutoCompleteItem(str: String) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(3.dp),
            color = Color.White,
            shadowElevation = 2.dp
        )
        {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(15.dp),
                    text = str,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = darkblue50
                )
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Arrow",
                    tint = darkblue50
                )
            }

        }

    }
}

@Composable
fun stock(viewModel: MainViewModel, itemState : ItemFormState,context : Context) {
    Column(modifier = Modifier
        .fillMaxWidth(),verticalArrangement = Arrangement.Top) {
        Surface(
            modifier = Modifier.padding(top = 5.dp, start = 5.dp,end = 5.dp),
            shape = RoundedCornerShape(7.dp),
            shadowElevation = 0.dp,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {
                Text(text = "Stock")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        OutlinedTextField(
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(bottom = 7.dp, start = 7.dp, end = 7.dp),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            value = itemState.stock,
                            onValueChange = {
                                viewModel.onItemFormEvent(itemFormEvent.itemStockChanged(it))
                            },
                            label = { Text(text = "Opening Stock") },
                            placeholder = { Text(text = "Ex: 200") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                        if (itemState.stockError != null) {
                            Text(
                                modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                                text = itemState.stockError,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                fontSize = 10.sp
                            )
                        }
                    }
                    com.vashishth.invoice.screens.DatePicker(viewModel,itemState,context)
                }
            }
        }
    }
}

@Composable
fun pricing(viewModel: MainViewModel,itemState: ItemFormState){
    var mExpanded by remember { mutableStateOf(false) }
    val GSTS = listOf("None","Exempted", "GST@0%", "IGST@0%", "GST@0.25%", "IGST@0.25%", "GST@3%", "IGST@3%","GST@5%","IGST@5%","GST@12%","IGST@12%","GST@18%","IGST@18%","GST@28%","IGST@28%")
    var mTextFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero)}
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Surface(modifier = Modifier.padding(top = 5.dp, start = 5.dp,end = 5.dp),
            shape = RoundedCornerShape(7.dp),
            shadowElevation = 0.dp,
            tonalElevation = 4.dp) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Pricing")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        OutlinedTextField(
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(bottom = 7.dp, start = 7.dp, end = 7.dp),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            value = itemState.purchasePrice,
                            onValueChange = {
                                viewModel.onItemFormEvent(itemFormEvent.itemPurchasePriceChanged(it))
                            },
                            label = { Text(text = "Purchase Price") },
                            placeholder = { Text(text = "Enter Purchase Price") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                        if (itemState.purchasePriceError != null) {
                            Text(
                                modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                                fontSize = 10.sp,
                                text = itemState.purchasePriceError,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    OutlinedTextField(singleLine = true,
                        value = if (itemState.gstRate.isNullOrBlank()) {
                            ""
                        } else {
                            itemState.gstRate
                        },
                        onValueChange = {
                            viewModel.onItemFormEvent(
                                itemFormEvent.itemGSTRateChanged(
                                    it
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 7.dp, bottom = 7.dp, end = 7.dp)
                            .onGloballyPositioned { coordinates ->
                                mTextFieldSize = coordinates.size.toSize()
                            },
                        label = { Text("GST") },
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            Icon(icon, "contentDescription",
                                Modifier.clickable { mExpanded = !mExpanded })
                        }
                    )
                    DropdownMenu(
                        expanded = mExpanded,
                        onDismissRequest = { mExpanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })

                    ) {
                        GSTS.forEach { label ->
                            DropdownMenuItem(onClick = {
                                viewModel.onItemFormEvent(itemFormEvent.itemGSTRateChanged(label))
                                mExpanded = false
                            }) {
                                Text(text = label)
                            }
                        }
                    }
                }

                Column {
                    OutlinedTextField(
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = itemState.price,
                        onValueChange = {
                            viewModel.onItemFormEvent(itemFormEvent.itemPriceChanged(it))
                        },
                        label = { Text(text = "Sale Price") },
                        placeholder = { Text(text = "Enter Item Sale Price") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    if (itemState.priceError != null) {
                        Text(
                            modifier = Modifier.padding(end = 10.dp).align(Alignment.End),
                            text = itemState.priceError,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePicker(viewModel: MainViewModel, itemState: ItemFormState, mContext : Context){
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
            viewModel.onItemFormEvent(itemFormEvent.itemStockAddDAteChanged("$mDayOfMonth-${mMonth+1}-$mYear"))
        }, mYear, mMonth, mDay
    )
    androidx.compose.material.Surface(
        modifier = Modifier.padding(1.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        onClick = { mDatePickerDialog.show() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material.Text(text = "${itemState.stockAddDate}")
            androidx.compose.material.Icon(
                modifier = Modifier.size(25.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar"
            )
        }
    }
}
