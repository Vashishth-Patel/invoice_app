package com.vashishth.invoice.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
    object BusinessDetails : BottomNavItem(
        name = "About",
        route = Screen.updateBusinessDetailsScreen.route,
        Icon = Icons.Filled.Business
    )
}

