package com.vashishth.invoice.components


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vashishth.invoice.data.entity.Customer
import com.vashishth.invoice.R
import com.vashishth.invoice.ui.theme.bluelight1
import com.vashishth.invoice.ui.theme.darkblue30
import com.vashishth.invoice.ui.theme.darkblue40
import com.vashishth.invoice.utils.swipeToDelete
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.ui.graphics.drawscope.translate as translate1

@ExperimentalAnimationApi
@Composable
fun customerItemView(
    regNo : Int,
    name : String,
    phone : Long?,
    modifier: Modifier = Modifier,
    customer: Customer,
    onDeleted: () -> Unit
    ){
    val particleRadiusDp = dimensionResource(id = R.dimen.particle_radius)
    val particleRadius: Float
    val itemHeightDp = dimensionResource(id = R.dimen.image_size)
    val itemHeight: Float
    val explosionParticleRadius: Float
    val explosionRadius: Float
    with(LocalDensity.current) {
        particleRadius = particleRadiusDp.toPx()
        itemHeight = itemHeightDp.toPx()
        explosionParticleRadius = dimensionResource(id = R.dimen.explosion_particle_radius).toPx()
        explosionRadius = dimensionResource(id = R.dimen.explosion_radius).toPx()
    }
    val screenWidth: Int
    with(LocalConfiguration.current) {
        screenWidth = this.screenWidthDp
    }
    val radius = itemHeight * 0.5f
    val funnelWidth = radius * 3
    val sideShapeWidth = funnelWidth + particleRadius * 2

   val offsetX =  remember{
       Animatable(0f)
   }

    val explosionPercentage = remember { mutableStateOf(0f) }

    val funnelInitialTranslation = -funnelWidth - particleRadius
    val funnelTranslation = remember { mutableStateOf(funnelInitialTranslation) }
    funnelTranslation.value = (offsetX.value + funnelInitialTranslation).negateIfPositive {
        explosionPercentage.value = (offsetX.value + funnelInitialTranslation) / screenWidth
    }


    Box {
        Canvas(
            Modifier.height(itemHeightDp)
        ) {
            translate1(funnelTranslation.value) {
                drawPath(
                    path = drawFunnel(
                        upperRadius = radius,
                        lowerRadius = particleRadius * 3 / 4f,
                        width = funnelWidth
                    ),
                    color = Color.Red
                )
            }
            translate1(offsetX.value - particleRadius) {
                drawCircle(color = Color.Red, radius = particleRadius)
            }
        }
        Canvas(modifier = Modifier
            .height(itemHeightDp)
            .offset {
                IntOffset(
                    (offsetX.value.roundToInt() - 2 * particleRadius.toInt()).coerceAtMost(
                        funnelWidth.toInt()
                    ), 0
                )
            })
        {
            val numberOfExplosionParticles = 10
            val particleAngle = Math.PI * 2 / numberOfExplosionParticles
            var angle = 0.0
            repeat(numberOfExplosionParticles / 2 + 1) {
                val hTranslation = (cos(angle).toFloat() * explosionRadius) * explosionPercentage.value
                val vTranslation = (sin(angle).toFloat() * explosionRadius) * explosionPercentage.value

                translate1(hTranslation, vTranslation) {
                    drawCircle(
                        color = Color.Red,
                        radius = explosionParticleRadius,
                        alpha = explosionPercentage.value / 2
                    )
                }
                if (angle != 0.0 && angle != Math.PI) {
                    translate1(hTranslation, -vTranslation) {
                        drawCircle(
                            color = Color.Red,
                            radius = explosionParticleRadius,
                            alpha = explosionPercentage.value / 2
                        )
                    }
                }
                angle += particleAngle
            }
        }

        Box(
            Modifier
                .padding(horizontal = 16.dp)
                .swipeToDelete(offsetX, maximumWidth = sideShapeWidth) {
                    onDeleted()
                }
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = bluelight1
                    )
                    .padding(dimensionResource(id = R.dimen.slot_padding))
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(),
            ) {
                Text(
                    customer.name.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = darkblue40
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "${customer.phoneNumber}", fontSize = 10.sp, color = darkblue30)
                }
            }
        }
    }

}


private fun Float.negateIfPositive(onPositive: () -> Unit): Float {
    return if (this > 0) {
        onPositive()
        -this
    } else this
}


