package com.vashishth.invoice.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vashishth.invoice.data.entity.Address
import com.vashishth.invoice.data.entity.Business
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.screens.viewModels.MainViewModel
import com.vashishth.invoice.ui.theme.*

@Composable
fun businessDetailCard(navController: NavController,viewModel: MainViewModel){
    if(viewModel.checkBusinessDetail() > 0){
        businessDetailCard(viewModel)
    }else{
        cardWithButton(navController = navController, viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cardWithButton(viewModel: MainViewModel,navController: NavController){

    var businessNameList : List<String> = viewModel.getBusinessNameList()
    var businessName1 : String = businessNameList[0]

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        border  = BorderStroke(1.dp, Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Green0)
        ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Text(modifier = Modifier.fillMaxWidth(),text = businessName1.uppercase(), fontSize = 25.sp, fontWeight = FontWeight.Bold, color = darkblue40, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(14.dp))
            Row (modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start,){
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Setting Icon", tint = darkblue40)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Business Setting", fontSize = 23.sp, color = darkblue40)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Business info, Logo, Address, Email, \nSignature and more", textAlign = TextAlign.Start, color = darkblue40)
        }
        AddBtn(
            Modifier
                .height(65.dp)
                .fillMaxWidth(),
            backGroundColor = green10,
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            borderStroke = BorderStroke(1.dp, green10),
            elevation = 5.dp,
            onClick = { navController.navigate(Screen.addBusinessDetailsScreen.route) },
            title = "FILL DETAILS")

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun businessDetailCard(viewModel: MainViewModel){
    var businessDetailList : List<Business> = viewModel.getBusinessDetail()
    var currentBusiness : Business = businessDetailList[0]
    var addressList : List<Address> = viewModel.getAddressList()
    var currentAddress : Address = addressList[0]
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        border  = BorderStroke(1.dp, Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Green0)
    ){
        Column() {

            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),text = currentBusiness.legalName.toString().uppercase(), textAlign = TextAlign.Center,fontSize = 25.sp, fontWeight = FontWeight.Bold, color = darkblue40)
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), horizontalAlignment = Alignment.Start) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),text = "GSTIN - ${if(currentBusiness.Gstin != null){currentBusiness.Gstin}else{""}}", textAlign = TextAlign.End, fontSize = 10.sp, color = darkblue30)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = "Phone Icon",
                        tint = darkblue40
                    )
                    Text(modifier = Modifier.padding(start = 5.dp),
                        text = currentBusiness.phoneNumber.toString(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = darkblue40
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "email Icon",
                        tint = darkblue40
                    )
                    Text(modifier = Modifier.padding(start = 5.dp),
                        text = if(currentBusiness.email.isNullOrBlank()){""}else{currentBusiness.email.toString()},
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = darkblue40
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Filled.Web,
                        contentDescription = "Web Icon",
                        tint = darkblue40
                    )
                    Text(modifier = Modifier.padding(start = 5.dp),
                        text = if(currentBusiness.website.isNullOrBlank()){""}else{currentBusiness.website.toString()},
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = darkblue40
                    )
                }
                Row {
                    Icon(modifier = Modifier.padding(top =4.dp),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location Icon",
                        tint = darkblue40
                    )
                    Text(modifier = Modifier.padding(start = 5.dp),
                        text = "${currentAddress.address}\n${currentAddress.city}, ${currentAddress.PINCode}, ${currentAddress.state}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = darkblue40
                    )
                }
                
            }


        }
    }

}