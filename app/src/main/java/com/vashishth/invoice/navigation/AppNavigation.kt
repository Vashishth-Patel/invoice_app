package com.vashishth.invoice.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.vashishth.invoice.screens.*
import com.vashishth.invoice.screens.AddBusiness.AddBusinessDetailsScreen
import com.vashishth.invoice.screens.AddBusiness.AddBusinessScreen
import com.vashishth.invoice.screens.AddItem.AddInvoiceItemScreen


@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun AppNavigation(){
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController = navController, startDestination = Screen.homeScreen.route){
        composable(Screen.homeScreen.route,
            enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            //here we pass where this should lead us to

            HomeScreen(navController = navController)

        }

        composable(Screen.customerListScreen.route,
            enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            //here we pass where this should lead us to
            CustomerListScreen(navController = navController)
        }

        composable(Screen.customerDetailScreen.route+"/{customer}",
        arguments = listOf(navArgument("customer"){type = NavType.StringType }),enterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            backStackEntry ->

            //here we pass where this should lead us to
            CustomerDetailScreen(navController = navController, customerregNo = backStackEntry.arguments?.getString("customer"))
        }

        composable(Screen.itemListScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            //here we pass where this should lead us to
            ItemListScreen(navController = navController)
        }

        composable(Screen.itemDetailScreen.route+"/{item}",
            arguments = listOf(navArgument("item"){type = NavType.StringType }),enterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }
        ){
                backStackEntry ->
            //here we pass where this should lead us to
            ItemDetailScreen(navController = navController, itemregNo = backStackEntry.arguments?.getString("item"))
        }

        composable(Screen.salesScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            //here we pass where this should lead us to
            SalesScreen(navController = navController)
        }

        composable(Screen.reportsScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            //here we pass where this should lead us to
            ReportScreen(navController = navController)
        }
        composable(Screen.invoicesScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            InvoicesScreen(navController = navController)
        }
        
        composable(Screen.addCustomerScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            AddCustomerScreen(navController = navController)
        }

        composable(Screen.addBusinessNameScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            AddBusinessScreen(navController)
        }
        composable(Screen.updateBusinessDetailsScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            updateBusinessDetailsScreen(navController = navController)
        }
        
        composable(Screen.addBusinessDetailsScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            AddBusinessDetailsScreen(navController = navController)
        }
        composable(Screen.addItemScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            addItemScreen(navController = navController)
        }
        composable(Screen.GenerateInvoiceScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            GenerateInvoiceScreen(navController = navController)
        }
        composable(Screen.AddInvoiceItemScreen.route,enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        },
            exitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "Red" ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }){
            AddInvoiceItemScreen(navController = navController)
        }

    }
}