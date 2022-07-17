package com.vashishth.invoice.screens

import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vashishth.invoice.components.BottomBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController as NavHostController)
        }
    ) {

    }
}