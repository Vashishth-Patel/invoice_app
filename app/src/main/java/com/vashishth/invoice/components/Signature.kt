package com.vashishth.invoice.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.applyCanvas
import com.vashishth.invoice.data.entity.BusinessSign
import com.vashishth.invoice.model.PathState
import com.vashishth.invoice.screens.viewModels.MainViewModel
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@Composable
fun SignatureDialog(viewModel: MainViewModel,
    isDialogOpen: MutableState<Boolean>,
    capturingViewBound: MutableState<Rect?>,
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    usedColors: MutableState<MutableSet<Color>>,
    paths: MutableState<MutableList<PathState>>,
    image: MutableState<Bitmap?>
) {
    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = true }, //outside click listener set false
            properties = DialogProperties(usePlatformDefaultWidth = false) // set Dialog fullView
        ) {
            val view = LocalView.current //get current view
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(250.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .weight(0.8f)
                        .onGloballyPositioned {
                            capturingViewBound.value = it.boundsInRoot()
                        }
                ) {
                    DrawingCanvas(
                        drawColor = drawColor,
                        drawBrush = drawBrush,
                        usedColors = usedColors,
                        paths = paths.value
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                paths.value = mutableListOf()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Clear")
                        }
                        Button(
                            onClick = {
                                val bounds = capturingViewBound.value ?: return@Button
                                image.value = Bitmap.createBitmap(
                                    bounds.width.roundToInt(), bounds.height.roundToInt(),
                                    Bitmap.Config.ARGB_8888
                                ).applyCanvas {
                                    translate(-bounds.left, -bounds.top)
                                    view.draw(this)
                                }
                                //TODO
                                viewModel.insertSign(BusinessSign(fromBitmap1(bitmap = image.value!!)))
                                //
                                isDialogOpen.value = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Submit")
                        }
                    }
                }
            }
        }
    }
}


    fun toBitmap1(byteArray: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

    fun fromBitmap1(bitmap: Bitmap) : ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
        return outputStream.toByteArray()

    }