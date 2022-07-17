package com.vashishth.invoice.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vashishth.invoice.ui.theme.*


@Composable
fun TextCard(
    title : String,
    description : String,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
){
    Surface(modifier = modifier
        .padding(5.dp)
        .height(80.dp)
        .clickable { onClick.invoke() },
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        color = bluelight1,
        border = BorderStroke(0.3.dp, blue0),
        shadowElevation =  4.dp,
    ) {
        Row(Modifier.padding(start = 35.dp, end = 5.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

            Column() {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    color = darkblue40,
                    fontSize = 18.sp
                )
                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    color = darkblue30,
                    fontSize = 9.sp
                )
            }
            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Arrow forwrd", tint = darkblue40)
        }
    }
}

@Composable
fun AddBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title:String,
    backGroundColor: Color = blueBtn,
    elevation: Dp = 0.dp,
    borderStroke: BorderStroke = BorderStroke(1.dp, blue30),
    shape: Shape = RoundedCornerShape(corner = CornerSize(35.dp))
){
    Surface(modifier = modifier
        .padding(all = 15.dp)
        .height(60.dp)
        .width(160.dp)
        .clickable { onClick.invoke() },
        shape = shape,
        border = borderStroke,
        color = backGroundColor,
        shadowElevation = elevation) {
        Row(Modifier.padding(5.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = title, modifier = modifier.padding(0.dp), textAlign = TextAlign.Center, color = darkblue40)
        }
    }
}