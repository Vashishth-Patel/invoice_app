package com.vashishth.invoice.components


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vashishth.invoice.data.entity.Customer
import com.vashishth.invoice.R
import com.vashishth.invoice.ui.theme.bluelight1
import com.vashishth.invoice.ui.theme.darkblue30
import com.vashishth.invoice.ui.theme.darkblue40
import com.vashishth.invoice.ui.theme.darkblue50
import com.vashishth.invoice.utils.swipeToDelete
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.ui.graphics.drawscope.translate as translate1

@ExperimentalAnimationApi
@Composable
fun customerItemView(
    modifier: Modifier = Modifier,
    customer: Customer,
    onClick: (String) -> Unit
    ){
        Surface(
            Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clickable { onClick(customer.customerRegNo.toString()) }
                .clip(RoundedCornerShape(8.dp)),
            tonalElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.slot_padding))
                    .fillMaxWidth(),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = modifier
                            .background(Color.LightGray, RoundedCornerShape(10.dp))
                            .padding(2.dp)
                            .size(35.dp),
                        text = customer.name.first().toString().uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(
                        customer.name.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        color = darkblue50
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(modifier = modifier
                        .size(23.dp)
                        .padding(end = 5.dp),imageVector = Icons.Filled.Call, contentDescription = "call Icon", tint = darkblue40)
                    Text(text = "${customer.phoneNumber}", fontSize = 13.sp, color = darkblue40)
                }
            }
        }
    }



