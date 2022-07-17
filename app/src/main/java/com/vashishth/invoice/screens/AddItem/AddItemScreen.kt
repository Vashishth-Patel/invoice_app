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
import androidx.compose.material.TabRowDefaults
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
import com.google.accompanist.pager.*
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.components.DropDownSearchBar
import com.vashishth.invoice.components.TextSearchBar
import com.vashishth.invoice.model.hsnCodeItem
import com.vashishth.invoice.model.unit
import com.vashishth.invoice.screens.AddCustomer.AllFormEvent
import com.vashishth.invoice.screens.AddItem.ItemFormState
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.*
import com.vashishth.invoice.utils.autoComplete.AutoCompleteBox
import com.vashishth.invoice.utils.autoComplete.AutoCompleteSearchBarTag
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

private fun getJSONFromAssets(context: Context, fileName: String): String? {
    var json: String? = null
    val charset: Charset = Charsets.UTF_8
    try {
        val myUsersJSONFile = context.assets.open(fileName)
        val size = myUsersJSONFile.available()
        val buffer = ByteArray(size)
        myUsersJSONFile.read(buffer)
        myUsersJSONFile.close()
        json = String(buffer, charset)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}

val hsnList: ArrayList<hsnCodeItem> = ArrayList()
fun hsnLoad(context: Context) {
    if (hsnList.isEmpty()) {
        try {

            val obj = JSONObject(getJSONFromAssets(context, "hsn.json")!!)
            val HsnArray = obj.getJSONArray("HSN")

            for (i in 0 until HsnArray.length()) {
                val HSNCODE1 = HsnArray.getJSONObject(i)
                val hsncode = HSNCODE1.getString("HSN Code")
                val hsnDescription = HSNCODE1.getString("HSN Description")
                val hsnDetails =
                    hsnCodeItem(hsncode, hsnDescription)
                hsnList.add(hsnDetails)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

val unitsList: ArrayList<unit> = ArrayList()
fun unitsLoad(context: Context) {
    if (unitsList.isEmpty()) {
        try {
            val obj = JSONObject(getJSONFromAssets(context, "units_list.json")!!)
            val UnitsArray = obj.getJSONArray("UNITS")

            for (i in 0 until UnitsArray.length()) {
                val Unit1 = UnitsArray.getString(i)
                unitsList.add(unit(Unit1))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class
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
                btmSheet1(viewModel)
            } else {
                btmSheet2(viewModel)
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
        coroutineScope.launch {
            hsnLoad(context = context)
            unitsLoad(context = context)
        }
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
            val itemState = viewModel.itemState
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = itemState.itemName,
                        onValueChange = {
                            viewModel.onEvent(AllFormEvent.itemNameChanged(it))
                        },
                        label = { Text(text = "Item Name") },
                        placeholder = { Text(text = "Enter Item Name") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                    if (itemState.itemNameError != null) {
                        Text(
                            text = itemState.itemNameError,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.End
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        value = itemState.unit,
                        onValueChange = {
                            viewModel.onEvent(AllFormEvent.itemUnitChanged(it))
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
                        Text(
                            text = itemState.unitError,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.End
                        )
                    }
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    value = if(itemState.hsnNumber.isNullOrBlank()){""}else{itemState.hsnNumber},
                    onValueChange = {
                        viewModel.onEvent(AllFormEvent.itemHSNNumberChanged(it))
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
                    keyboardActions = KeyboardActions(onDone = { }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    )
                )
                if (itemState.hsnNumberError != null) {
                    Text(
                        text = itemState.hsnNumberError,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.End
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    singleLine = true,
                    value = if(itemState.description.isNullOrBlank()){""}else{itemState.description},
                    //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    onValueChange = {
                        viewModel.onEvent(AllFormEvent.itemDescriptionChanged(it))
                    },
                    label = { Text(text = "Item Description") },
                    placeholder = { Text(text = "Enter Item Description") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                if (itemState.descriptionError != null) {
                    Text(
                        text = itemState.descriptionError,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.End
                    )
                }
                tabLayout(viewModel,itemState)
                AddBtn(modifier = Modifier.fillMaxWidth(),onClick = {
                    viewModel.onEvent(AllFormEvent.AddItem)
                }, title = "SAVE ITEM", shape = RoundedCornerShape(8.dp))
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun btmSheet1(viewModel: MainViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
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
                    viewModel.onEvent(AllFormEvent.itemUnitChanged(unit.unit))
                    filter(valueUnit)
                    if (keyboardController != null) {
                        keyboardController.hide()
                    }
                    viewUnit.clearFocus()
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun btmSheet2(viewModel: MainViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
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
                    viewModel.onEvent(AllFormEvent.itemHSNNumberChanged(hsnCodeItem.Code))
                    view.clearFocus()
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

@OptIn(ExperimentalPagerApi::class, ExperimentalUnitApi::class)
@Composable
fun tabLayout(viewModel: MainViewModel,itemState: ItemFormState){
    val pagerState = rememberPagerState(pageCount = 2)
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState,viewModel,itemState)
    }
}


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {

    val list = listOf(
        "Pricing" to Icons.Default.PriceChange,
        "Stock" to Icons.Default.Storage,
    )
    val scope = rememberCoroutineScope()

    androidx.compose.material.TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = bluelight1,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = bluelight
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            androidx.compose.material3.Tab(
                icon = {
                    Icon(imageVector = list[index].second, contentDescription = null)
                },
                text = {
                    Text(
                        list[index].first,
                        color = if (pagerState.currentPage == index) bluelight else blue0
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState,viewModel: MainViewModel,itemState: ItemFormState) {
    HorizontalPager(state = pagerState) {
            page ->
        when (page) {
            0 -> pricing(viewModel = viewModel,itemState = itemState)
            1 -> stock(viewModel = viewModel, itemState = itemState)
        }
    }
}

@Composable
fun stock(viewModel: MainViewModel, itemState : ItemFormState) {
    Column(modifier = Modifier.fillMaxHeight(.75f).fillMaxWidth(),verticalArrangement = Arrangement.Top) {
        Surface(
            modifier = Modifier.padding(5.dp),
            shape = RoundedCornerShape(7.dp),
            shadowElevation = 0.dp,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .padding(bottom = 7.dp, start = 7.dp, end = 7.dp),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = itemState.stock,
                        onValueChange = {
                            viewModel.onEvent(AllFormEvent.itemStockChanged(it))
                        },
                        label = { Text(text = "Opening Stock") },
                        placeholder = { Text(text = "Ex: 200") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    com.vashishth.invoice.screens.MyContent(viewModel,itemState)
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp, start = 7.dp, end = 7.dp),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    value = if(itemState.itemNumber.isNullOrBlank()){""}else{itemState.itemNumber},
                    onValueChange = {
                        viewModel.onEvent(AllFormEvent.itemNumberChanged(it))
                    },
                    label = { Text(text = "Item Number") },
                    placeholder = { Text(text = "Barcode/Item Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }
        }
    }
}



@Composable
fun pricing(viewModel: MainViewModel,itemState: ItemFormState){
    val scrollState = rememberScrollState()
    var mExpanded by remember { mutableStateOf(false) }
    val GSTS = listOf("None","Exempted", "GST@0%", "IGST@0%", "GST@0.25%", "IGST@0.25%", "GST@3%", "IGST@3%","GST@5%","IGST@5%","GST@12%","IGST@12%","GST@18%","IGST@18%","GST@28%","IGST@28%")

    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero)}
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column(
        modifier = Modifier.fillMaxHeight(.75f)
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Surface(modifier = Modifier.padding(5.dp),shape = RoundedCornerShape(7.dp), shadowElevation = 0.dp, tonalElevation = 4.dp) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {
                Text(text = "Pricing")
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    value = itemState.purchasePrice,
                    onValueChange = {
                        viewModel.onEvent(AllFormEvent.itemPurchasePriceChanged(it))
                    },
                    label = { Text(text = "Purchase Price") },
                    placeholder = { Text(text = "Enter Item Purchase Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    value = itemState.price,
                    onValueChange = {
                        viewModel.onEvent(AllFormEvent.itemPriceChanged(it))
                    },
                    label = { Text(text = "Sale Price") },
                    placeholder = { Text(text = "Enter Item Sale Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }
        }

        Surface(modifier = Modifier.padding(5.dp),shape = RoundedCornerShape(7.dp), shadowElevation = 0.dp, tonalElevation = 4.dp) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {
                Text(text = "Taxes")
                OutlinedTextField(
                    value = if(itemState.gstRate.isNullOrBlank()){""}else{itemState.gstRate},
                    onValueChange = { viewModel.onEvent(AllFormEvent.itemGSTRateChanged(it))},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = {Text("GST")},
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(icon,"contentDescription",
                            Modifier.clickable { mExpanded = !mExpanded })
                    }
                )
                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})

                ) {
                    GSTS.forEach { label ->
                        DropdownMenuItem(onClick = {
                            viewModel.onEvent(AllFormEvent.itemGSTRateChanged(label))
                            mExpanded = false
                        }) {
                            Text(text = label)
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyContent(viewModel: MainViewModel,itemState: ItemFormState){
    val mContext = LocalContext.current
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
            viewModel.onEvent(AllFormEvent.itemStockAddDAteChanged("$mDayOfMonth-${mMonth+1}-$mYear"))
        }, mYear, mMonth, mDay
    )
    androidx.compose.material.Surface(
        modifier = Modifier.padding(1.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        onClick = { mDatePickerDialog.show() }) {
        Row(
            modifier = Modifier.fillMaxWidth()
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