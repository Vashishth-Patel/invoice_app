package com.vashishth.invoice.components

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.TypedArrayUtils.getText
import com.vashishth.invoice.data.entity.Product
import com.vashishth.invoice.ui.theme.darkblue50
import java.util.Locale

@Composable
fun itemView(
    item :  Product,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
){
    val currency : String = LocalContext.current.getString(com.vashishth.invoice.R.string.currency)

    Surface(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {
            onClick.invoke()
        },
        shape = RoundedCornerShape(5.dp),
        tonalElevation = 16.dp,
        contentColor = darkblue50

    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = modifier.fillMaxWidth(0.9f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = modifier
                            .background(Color.LightGray, RoundedCornerShape(10.dp))
                            .padding(2.dp)
                            .size(35.dp),
                        text = item.itemName.first().toString().uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = item.itemName.replaceFirstChar {if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    )
                }
                Row(modifier = modifier.fillMaxWidth().padding(start = 17.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                    Column() {
                        Text(text = "Sale Price")
                        Text(modifier = modifier.alpha(0.7f),
                            text = "$currency ${item.price}",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light)
                    }
                    Column() {
                        Text(text = "Purchase Price")
                        Text(modifier = modifier.alpha(0.7f),
                            text = "$currency ${item.purchasePrice}",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light
                            )
                    }
                }
            }
            Column(modifier = modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
                ) {
                Text(text = "${item.stock}")
                Text(text = getUnitShortForm(item.unit))
            }
        }
    }

}

fun getUnitShortForm(unit : String):String{
    return unit.substring(unit.indexOf("(")+1,unit.indexOf(")")).uppercase()
}