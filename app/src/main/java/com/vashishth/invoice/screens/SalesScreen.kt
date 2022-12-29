package com.vashishth.invoice.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vashishth.invoice.R
import com.vashishth.invoice.data.entity.InvoiceItem
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
import com.vashishth.invoice.screens.viewModels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("RememberReturnType")
@Composable
fun SalesScreen(navController: NavController,viewModel : MainViewModel = hiltViewModel()) {
    val df = SimpleDateFormat("dd-mm-yyyy")
    val invoices = viewModel.invoice.collectAsState().value
    var date = remember{
        mutableStateOf("21-12-2022")
    }
    val context = LocalContext.current

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(10.dp)) {
            DatePicker3(date = date, mContext = context)
            Log.d("Galaxy","${date.value}")
            if (!invoices.filter { df.format(it.invoice.entryDate) == date.value }.isEmpty()) {
                salesView(invoices = invoices.filter { df.format(it.invoice.entryDate).toString() == date.value }, date.value)
            }else{
                    Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                        androidx.compose.material3.Text(
                            text = "No Sales on this day",
                            color = Color.Red
                        )
                    }

            }
            }
        }
    }

@Composable
fun salesView(invoices:List<InvoiceWithInvoiceItems>,date:String){
    var invoiceItems : List<InvoiceItem> = emptyList()
    invoices.forEach {
        invoiceItems += it.invoiceItems
    }
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)
    var total = 0.0
    invoiceItems.forEach { total += (it.quantity * it.unitPrice) }


    androidx.compose.material3.Surface(modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        tonalElevation = 5.dp,
        shadowElevation = 3.dp,
        contentColor = Color.Black
        ) {
        Column(Modifier.padding(15.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(15.dp))
            Text("${date}")
            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(){
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        androidx.compose.material3.Text(modifier = Modifier.fillMaxWidth(0.24f),
                            text =
                            "Bill No",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        androidx.compose.material3.Text(modifier = Modifier.fillMaxWidth(0.38f),
                            text =
                            "Item",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        androidx.compose.material3.Text(modifier = Modifier.fillMaxWidth(0.45f),
                            text =
                            "Qty",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        androidx.compose.material3.Text(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            text =
                            "Price",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                item{
                    Spacer(modifier = Modifier.height(13.dp))
                }

                items(invoiceItems){item ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(modifier = Modifier
                            .fillMaxWidth(0.18f)
                            .padding(start = 20.dp),text = item.invoiceNumber.toString())
                        androidx.compose.material3.Text(modifier = Modifier.fillMaxWidth(0.45f),text = item.itemName)
                        androidx.compose.material3.Text(modifier = Modifier.fillMaxWidth(0.3f),text = item.quantity.toString())
                        androidx.compose.material3.Text(modifier = Modifier.fillMaxWidth(0.6f),text = "₹" +item.unitPrice.toString())
                    }
                    Spacer(modifier = Modifier.height(13.dp))
                }
            }
            Spacer(modifier = Modifier.height(0.dp))

            Divider(color = Color.Black, thickness = 1.dp)

            Spacer(modifier = Modifier.height(13.dp))

            Row(Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                androidx.compose.material3.Text(
                    text = "Total",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold
                    )
                )
                androidx.compose.material3.Text(
                    "$currency" + total.toString(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

    }
}




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePicker3(modifier: Modifier = Modifier,
                date:MutableState<String>,
                mContext : Context
){
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
            date.value = "$mDayOfMonth-${mMonth+1}-$mYear"
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
            Text(text = "${date.value}")
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar"
            )
        }
    }
}