package com.vashishth.invoice.screens.AddBusiness


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.R
import com.vashishth.invoice.components.AddBtn
import com.vashishth.invoice.components.SignatureDialog
import com.vashishth.invoice.model.PathState
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.AddCustomer.businessFormEvent
import com.vashishth.invoice.screens.viewModels.MainViewModel
import kotlinx.coroutines.delay


@SuppressLint("SuspiciousIndentation")
@Composable
fun AddBusinessScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()){

    val context = LocalContext.current
        Surface {
            val state = viewModel.businessState1
            LaunchedEffect(key1 = context){
                viewModel.validationEvents.collect{event ->
                    when(event){
                        is MainViewModel.ValidationEvent.Success ->{
                            navController.navigate(Screen.homeScreen.route)
                            Toast.makeText(
                                context,
                                "BUSINESS CREATED",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp), verticalArrangement = Arrangement.Center) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_invoice),
                        contentDescription = "invoive Image"
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Enter Your Business Name", textAlign = TextAlign.Start, fontSize = 15.sp)
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        value = state.name,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = "Business Name Icon"
                            )
                        },
                        onValueChange = {
                            viewModel.onBusinessEvent(businessFormEvent.businessNameChanged(it))
                        },
                        label = { Text(text = "Business Name") },
                        placeholder = { Text(text = "Enter Your Business Name") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                    if (state.nameError != null) {
                        Text(
                            fontSize = 10.sp,
                            text = state.nameError,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.End).padding(start = 10.dp)
                        )
                    }
                    AddBtn(onClick = {
                            viewModel.onBusinessEvent(businessFormEvent.BusinessNameSubmit)
                    }, title = "Get Started")
                }
            }
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessDetailsScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()){

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val namePan = remember { mutableStateOf(true) }
    val phoneEmail = remember { mutableStateOf(false) }
    val address = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("About Business") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.homeScreen.route)}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
                )
        }
    ) { it ->
        val state = viewModel.businessState2
        LaunchedEffect(key1 = context){
            viewModel.validationEvents.collect{event ->
                when(event){
                    is MainViewModel.ValidationEvent.Success ->{
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
            Column {
                Column(
                    Modifier
                        .weight(0.67f)
                        .verticalScroll(scrollState)) {
                    Surface(
                        Modifier.padding(15.dp),
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = 5.dp
                    ) {
                        Column {
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
                                    Icon(imageVector = Icons.Default.Business, contentDescription = "")
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
                                    value = state.legalName,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.BusinessCenter,
                                            contentDescription = "Legal Name Icon"
                                        )
                                    },
                                    onValueChange = {newName ->
                                        viewModel.onBusinessEvent(businessFormEvent.legalNameChanged(newName))
                                    },
                                    label = { Text(text = "Legal Name Of Business") },
                                    placeholder = { Text(text = "Enter Legal Name Of Business") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.legalNameError != null) {
                                    Text(modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
                                        fontSize = 10.sp,
                                        text = state.legalNameError,
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(9.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    value = state.Gstin ?: "",
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.AddBusiness,
                                            contentDescription = "GSTIN Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.GSTINNumberChanged(it))
                                    },
                                    label = { Text(text = "GSTIN Number") },
                                    placeholder = { Text(text = "Enter GSTIN Nnumber") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.GstinError != null) {
                                    Text(
                                        text = state.GstinError,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
                                        fontSize = 10.sp
                                    )
                                }
                                Column{
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(9.dp),
                                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                        value = state.PANNumber ?: "",
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.CreditCard,
                                                contentDescription = "Credit Icon"
                                            )
                                        },
                                        onValueChange = {
                                            viewModel.onBusinessEvent(businessFormEvent.PANNumberChanged(it))
                                        },
                                        label = { Text(text = "Business PAN Number") },
                                        placeholder = { Text(text = "Enter Business PAN Number") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    if (state.PANNumberError != null) {
                                        Text(
                                            text = state.PANNumberError,
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
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
                        Column{
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
                                    value = state.phoneNumber,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "Phone Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.businessPhoneChanged(it))
                                    },
                                    label = { Text(text = "Business PhoneNumber") },
                                    placeholder = { Text(text = "Enter Business PhoneNumber") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.phoneNumberError != null) {
                                    Text(
                                        text = state.phoneNumberError,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
                                        fontSize = 10.sp
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(9.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    value = state.email ?: "",
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Email Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.businessEmailChanged(it))
                                    },
                                    label = { Text(text = "Business Email") },
                                    placeholder = { Text(text = "Enter Business Email") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.emailError != null) {
                                    Text(
                                        text = state.emailError,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
                                        fontSize = 10.sp
                                    )
                                }

                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(9.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    value = state.website ?: "",
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Web,
                                            contentDescription = "Web Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.websiteChanged(it))
                                    },
                                    label = { Text(text = "Business Website") },
                                    placeholder = { Text(text = "Enter Business Website") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.websiteError != null) {
                                    Text(
                                        text = state.websiteError,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
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
                        Column{
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
                                    value = state.address,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.AddLocation,
                                            contentDescription = "location Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.businessAddressChanged(it))
                                    },
                                    label = { Text(text = "Office/Shop Address") },
                                    placeholder = { Text(text = "Enter Office/Shop Address") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.addressError != null) {
                                    Text(
                                        text = state.addressError,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
                                        fontSize = 10.sp
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(9.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    value = state.PINCode ?: "",
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Pin,
                                            contentDescription = "pin Icon"
                                        )
                                    },
                                    //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.PINCodeChanged(it))
                                    },
                                    label = { Text(text = "PIN Code") },
                                    placeholder = { Text(text = "Enter PIN Code") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                if (state.PINCodeError != null) {
                                    Text(
                                        text = state.PINCodeError,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.align(Alignment.End).padding(start = 10.dp),
                                        fontSize = 10.sp
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(9.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    value = state.state ?: "",
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "State Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.StateChanged(it))
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
                                    value = state.city ?: "",
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.LocationCity,
                                            contentDescription = "city Icon"
                                        )
                                    },
                                    onValueChange = {
                                        viewModel.onBusinessEvent(businessFormEvent.CityChanged(it))
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

                Column (Modifier.weight(0.25f)){
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
                    AddBtn(Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.onBusinessEvent(businessFormEvent.BusinessDetailSubmit)

                        },
                        title = "SAVE DETAILS",
                        shape = RoundedCornerShape(10.dp))
                }
            }
        }
    }


}
    fun toBitmap1(byteArray: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

@SuppressLint("MutableCollectionMutableState")
@ExperimentalComposeUiApi
@Composable
fun CaptureSignature(viewModel : MainViewModel) {
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val capturingViewBounds = remember { mutableStateOf<Rect?>(null) }
    val image = remember { mutableStateOf<Bitmap?>(null) }
    val isDialogOpen = remember { mutableStateOf(false) }
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }
    val usedColors = remember { mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray)) }

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

    SignatureDialog(viewModel,
        isDialogOpen = isDialogOpen,
        capturingViewBound = capturingViewBounds,
        drawColor = drawColor,
        drawBrush = drawBrush,
        usedColors = usedColors,
        paths = paths,
        image = image
    )

    Spacer(modifier = Modifier.padding(top = 20.dp))

    AddBtn(modifier = Modifier
        .height(70.dp)
        .width(130.dp),
        fontSize = 18.sp,
        onClick = { isDialogOpen.value = true }, title = "Signature")

}

