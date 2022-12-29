package com.vashishth.invoice.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.data.entity.Address
import com.vashishth.invoice.data.entity.Business
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.AddBusiness.CaptureSignature
import com.vashishth.invoice.screens.AddBusiness.toBitmap1
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.validation.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun updateBusinessDetailsScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()){

    var businessDetailList : List<Business> = viewModel.getBusinessDetail()
    if (businessDetailList.isEmpty()){
        navController.navigate(Screen.addBusinessDetailsScreen.route)

    }else {

        var currentBusiness: Business = businessDetailList[0]
        var addressList: List<Address> = viewModel.getAddressList()
        var currentAddress: Address = addressList[0]

        var legalName = remember {
            mutableStateOf(currentBusiness.legalName)
        }
        var PANNumber = remember {
            mutableStateOf(currentBusiness.PANNumber)
        }
        var Gstin = remember {
            mutableStateOf(currentBusiness.Gstin)
        }
        var PhoneNumber = remember {
            mutableStateOf(currentBusiness.phoneNumber.toString())
        }
        var website = remember {
            mutableStateOf(currentBusiness.website)
        }
        var email = remember {
            mutableStateOf(currentBusiness.email)
        }
        var address1 = remember {
            mutableStateOf(currentAddress.address)
        }
        var state1 = remember {
            mutableStateOf(currentAddress.state)
        }
        var city = remember {
            mutableStateOf(currentAddress.city)
        }
        var PINNumber = remember {
            mutableStateOf(currentAddress.PINCode.toString())
        }


        val keyboardController = LocalSoftwareKeyboardController.current
        val context = LocalContext.current
        var namePan = remember { mutableStateOf(true) }
        var phoneEmail = remember { mutableStateOf(false) }
        var address = remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text("About Business") },
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
            val state = viewModel.businessState2
            LaunchedEffect(key1 = context) {
                viewModel.validationEvents.collect { event ->
                    when (event) {
                        is MainViewModel.ValidationEvent.Success -> {
                            Toast.makeText(
                                context,
                                "BUSINESS DETAILS ADDED",
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(1000)
                            navController.navigate(Screen.homeScreen.route)
                        }
                    }
                }
            }
            Surface(Modifier.padding(it)) {
                val scrollState = rememberScrollState()
                Column() {
                    Column(
                        Modifier
                            .weight(0.67f)
                            .verticalScroll(scrollState)
                    ) {
                        Surface(
                            Modifier.padding(15.dp),
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 5.dp
                        ) {
                            Column() {
                                Surface(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable { namePan.value = !namePan.value }) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Business,
                                            contentDescription = ""
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "Business Details", fontSize = 25.sp)
                                            Text("Business Name,GSTIN,PAN...", fontSize = 10.sp)
                                        }
                                        if (namePan.value) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropUp,
                                                contentDescription = ""
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Drop Down"
                                            )
                                        }
                                    }
                                }
                                if (namePan.value) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = legalName.value,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.BusinessCenter,
                                                contentDescription = "Legal Name Icon"
                                            )
                                        },
                                        onValueChange = {
                                            legalName.value = it
                                        },
                                        label = { Text(text = "Legal Name Of Business") },
                                        placeholder = { Text(text = "Enter Legal Name Of Business") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (legalName.value.isNullOrBlank()) {
                                        Text(
                                            modifier = Modifier.align(Alignment.End)
                                                .padding(start = 10.dp),
                                            fontSize = 10.sp,
                                            text = "This field can't be blank",
                                            color = MaterialTheme.colorScheme.error,
                                        )
                                    }
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = if (Gstin.value != null) {
                                            Gstin.value
                                        } else {
                                            ""
                                        }.toString(),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.AddBusiness,
                                                contentDescription = "GSTIN Icon"
                                            )
                                        },
                                        onValueChange = {
                                            Gstin.value = it
                                        },
                                        label = { Text(text = "GSTIN Number") },
                                        placeholder = { Text(text = "Enter GSTIN Nnumber") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (!Gstin.value.isNullOrBlank()) {
                                        if (!ValidateGSTIN().execute(Gstin.value).successful) {
                                            Text(
                                                text = "Enter valid GSTin",
                                                color = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.align(Alignment.End)
                                                    .padding(start = 10.dp),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                    Column() {
                                        OutlinedTextField(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(9.dp),
                                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                            value = (if (PANNumber.value == null) {
                                                ""
                                            } else {
                                                PANNumber.value
                                            }).toString(),
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.CreditCard,
                                                    contentDescription = "Credit Icon"
                                                )
                                            },
                                            onValueChange = {
                                                PANNumber.value = it
                                            },
                                            label = { Text(text = "Business PAN Number") },
                                            placeholder = { Text(text = "Enter Business PAN Number") },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next
                                            )
                                        )
                                        if (!ValidatePANNumber().execute(PANNumber.value).successful) {
                                            Text(
                                                text = "Enter valid PANNumber",
                                                color = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.align(Alignment.End)
                                                    .padding(start = 10.dp),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Surface(
                            Modifier.padding(15.dp),
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 5.dp
                        ) {
                            Column() {
                                Surface(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable { phoneEmail.value = !phoneEmail.value }) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContactPage,
                                            contentDescription = ""
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "Communication", fontSize = 25.sp)
                                            Text("Phone Number,Website,Email...", fontSize = 10.sp)
                                        }
                                        if (phoneEmail.value) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropUp,
                                                contentDescription = ""
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Drop Down"
                                            )
                                        }
                                    }

                                }
                                if (phoneEmail.value) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = PhoneNumber.value,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "Phone Icon"
                                            )
                                        },
                                        onValueChange = {
                                            PhoneNumber.value = it
                                        },
                                        label = { Text(text = "Business PhoneNumber") },
                                        placeholder = { Text(text = "Enter Business PhoneNumber") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (!ValidatePhone().execute(PhoneNumber.value).successful) {
                                        Text(
                                            text = "Enter valis phonenumber",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.End)
                                                .padding(start = 10.dp),
                                            fontSize = 10.sp
                                        )
                                    }
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = if (email.value != null) {
                                            email.value
                                        } else {
                                            ""
                                        }.toString(),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Email,
                                                contentDescription = "Email Icon"
                                            )
                                        },
                                        onValueChange = {
                                            email.value = it
                                        },
                                        label = { Text(text = "Business Email") },
                                        placeholder = { Text(text = "Enter Business Email") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (!ValidateEmail().execute(email.value).successful) {
                                        Text(
                                            text = "Enter valid email",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.End)
                                                .padding(start = 10.dp),
                                            fontSize = 10.sp
                                        )
                                    }

                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = if (website.value != null) {
                                            website.value
                                        } else {
                                            ""
                                        }.toString(),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Web,
                                                contentDescription = "Web Icon"
                                            )
                                        },
                                        onValueChange = {
                                            website.value = it
                                        },
                                        label = { Text(text = "Business Website") },
                                        placeholder = { Text(text = "Enter Business Website") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (!ValidateWebsite().execute(website.value).successful) {
                                        Text(
                                            text = "Enter valid website",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.End)
                                                .padding(start = 10.dp),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                        Surface(
                            Modifier.padding(15.dp),
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 5.dp
                        ) {
                            Column() {
                                Surface(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable { address.value = !address.value }) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = ""
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "Address", fontSize = 25.sp)
                                            Text("City,State,PIN Code...", fontSize = 10.sp)
                                        }
                                        if (address.value) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropUp,
                                                contentDescription = ""
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Drop Down"
                                            )
                                        }
                                    }

                                }
                                if (address.value) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = address1.value,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.AddLocation,
                                                contentDescription = "location Icon"
                                            )
                                        },
                                        onValueChange = {
                                            address1.value = it
                                        },
                                        label = { Text(text = "Office/Shop Address") },
                                        placeholder = { Text(text = "Enter Office/Shop Address") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (address1.value.isNullOrBlank()) {
                                        Text(
                                            text = "This field can't be blank",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.End)
                                                .padding(start = 10.dp),
                                            fontSize = 10.sp
                                        )
                                    }
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = if (PINNumber.value == "null") {
                                            ""
                                        } else {
                                            PINNumber.value
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Pin,
                                                contentDescription = "pin Icon"
                                            )
                                        },
                                        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                        onValueChange = {
                                            PINNumber.value = it
                                        },
                                        label = { Text(text = "PIN Code") },
                                        placeholder = { Text(text = "Enter PIN Code") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (!ValidatePIN().execute(PINNumber.value).successful) {
                                        Text(
                                            text = "Enter valid PIN number",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.End)
                                                .padding(start = 10.dp),
                                            fontSize = 10.sp
                                        )
                                    }
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = if (state1.value != null) {
                                            state1.value
                                        } else {
                                            ""
                                        }.toString(),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.MyLocation,
                                                contentDescription = "State Icon"
                                            )
                                        },
                                        onValueChange = {
                                            state1.value = it
                                        },
                                        label = { Text(text = "State") },
                                        placeholder = { Text(text = "Enter State") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = if (city.value != null) {
                                            city.value
                                        } else {
                                            ""
                                        }.toString(),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.LocationCity,
                                                contentDescription = "city Icon"
                                            )
                                        },
                                        onValueChange = {
                                            city.value = it
                                        },
                                        label = { Text(text = "City") },
                                        placeholder = { Text(text = "Enter City") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Column(Modifier.weight(0.25f)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp), horizontalArrangement = Arrangement.End
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                val signList = viewModel.signList.collectAsState().value
                                if (signList.isNotEmpty()) {
                                    Image(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .weight(0.5f)
                                            .padding(end = 20.dp),
                                        bitmap = toBitmap1(signList.last().signature).asImageBitmap(),
                                        contentDescription = ""
                                    )
                                }
                                CaptureSignature(viewModel)
                            }
                        }

                    }
                    Column(Modifier.weight(0.12f)) {
                        AddBtn(
                            Modifier.fillMaxWidth(),
                            onClick = {
                                if (!legalName.value.isNullOrBlank() && !PhoneNumber.value.isNullOrBlank() && !address1.value.isNullOrBlank()) {
                                    viewModel.updateBusinessDetails(
                                        legalName.value,
                                        PANNumber.value,
                                        Gstin.value,
                                        PhoneNumber.value.toLong(),
                                        email.value,
                                        website.value,
                                        currentBusiness.businessId
                                    )
                                    viewModel.updateAddress(
                                        address1.value,
                                        if (PINNumber.value.isNullOrBlank()) {
                                            null
                                        } else {
                                            PINNumber.value.toInt()
                                        },
                                        state1.value,
                                        city.value,
                                        1
                                    )
                                } else {
                                    Toast.makeText(context, "Fill the detais", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            title = "UPDATE DETAILS",
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }
    }
}