package com.vashishth.invoice

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi

import com.vashishth.invoice.navigation.AppNavigation
import com.vashishth.invoice.ui.theme.InvoiceTheme
import com.vashishth.invoice.validation.RequestPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
//                AppNavigation()
                RequestPermission(permission = Manifest.permission.SEND_SMS)
            }

        }
    }
}

@Composable
fun MyApp(content : @Composable () -> Unit){
    InvoiceTheme() {
        content()
    }
}

