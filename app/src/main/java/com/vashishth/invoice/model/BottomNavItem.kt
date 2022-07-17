package com.vashishth.invoice.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.vashishth.invoice.navigation.Screen

sealed class BottomNavItem(
    val name : String,
    val route : String,
    val Icon : ImageVector
){
    object Home : BottomNavItem(
        name = "Home",
        route = Screen.homeScreen.route,
        Icon = Icons.Filled.Home
    )
    object Customers : BottomNavItem(
        name = "Customers",
        route = Screen.customerListScreen.route,
        Icon = Icons.Filled.Person
    )
    object Items : BottomNavItem(
        name = "Items",
        route = Screen.itemListScreen.route,
        Icon = Icons.Filled.ShoppingCart
    )
    object Settings : BottomNavItem(
        name = "Settings",
        route = Screen.settingsScreen.route,
        Icon = Icons.Filled.Settings
    )
}

