package com.vashishth.invoice.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.vashishth.invoice.screens.*



@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun AppNavigation(){
    val navController = rememberAnimatedNavController()



    AnimatedNavHost(navController = navController, startDestination = Screen.homeScreen.route){
        composable(Screen.homeScreen.route){
            //here we pass where this should lead us to

            HomeScreen(navController = navController)

        }

        composable(Screen.customerListScreen.route){
            //here we pass where this should lead us to
            CustomerListScreen(navController = navController)
        }

        composable(Screen.customerDetailScreen.route){
            //here we pass where this should lead us to
            CustomerDetailScreen(navController = navController)
        }

        composable(Screen.itemListScreen.route){
            //here we pass where this should lead us to
            ItemListScreen(navController = navController)
        }

        composable(Screen.itemDetailScreen.route){
            //here we pass where this should lead us to
            ItemDetailScreen(navController = navController)
        }

        composable(Screen.salesScreen.route){
            //here we pass where this should lead us to
            SalesScreen(navController = navController)
        }

        composable(Screen.reportsScreen.route){
            //here we pass where this should lead us to
            ReportScreen(navController = navController)
        }

        composable(Screen.settingsScreen.route){
            //here we pass where this should lead us to
            SettingScreen(navController = navController)
        }
        
        composable(Screen.invoicesScreen.route){
            invoicesScreen(navController = navController)
        }
        
        composable(Screen.addCustomerScreen.route){
            AddCustomerScreen(navController = navController)
        }

        composable(Screen.addBusinessNameScreen.route){
            addBusinessScreen(navController)
        }
        
        composable(Screen.addBusinessDetailsScreen.route){
            addBusinessDetailsScreen(navController = navController)
        }
        composable(Screen.addItemScreen.route){
            addItemScreen(navController = navController)
        }

    }
}