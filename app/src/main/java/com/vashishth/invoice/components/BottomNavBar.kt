package com.vashishth.invoice.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vashishth.invoice.model.BottomNavItem
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.ui.theme.*

enum class MultiFloatingState{
    Exapanded,
    Collapsed
}
enum class identifier{
    addPerson,
    addItem
}

class minFabItem(
    val icon : ImageBitmap,
    val identifier: String
)

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Customers,
        BottomNavItem.Items,
        BottomNavItem.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation (
        backgroundColor = blueBtn,
        contentColor = darkblue40
            ){
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.name, fontSize = 12.sp, color = darkblue40)
        },
        icon = {
            Icon(
                imageVector = screen.Icon,
                contentDescription = "Navigation Icon",
                tint = darkblue40
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}


@Composable
fun MultiFloatingButton(
    multiFloatingState: MultiFloatingState,
    onMultiFabChange : (MultiFloatingState) -> Unit,
    items : List<minFabItem>,
    navController: NavController
){

    val transition = updateTransition(targetState = multiFloatingState, label = "transition")

    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultiFloatingState.Exapanded) 36f else 0f
    }

    val fabScale by transition.animateFloat(label = "FabScale") {
        if (it == MultiFloatingState.Exapanded) 85f else 0f
    }

    val alpha by transition.animateFloat(label = "Alpha", transitionSpec = { tween(durationMillis = 100)}) {
        if(it == MultiFloatingState.Exapanded) 1f else 0f
    }

    Column (horizontalAlignment = Alignment.End){
        if(transition.currentState == MultiFloatingState.Exapanded){
            items.forEach{
                minFab(item = it,
                    onMinFabItemClicked = {
                        when(it.identifier){
                            identifier.addItem.name -> {
                                navController.navigate(Screen.addItemScreen.route)
                            }
                            identifier.addPerson.name -> {
                                navController.navigate(Screen.addCustomerScreen.route)
                            }
                        }
                },
                    alpha = alpha,
                    fabScale = fabScale
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        FloatingActionButton(shape = CircleShape, containerColor = blueBtn,onClick = {
            onMultiFabChange(
                if (transition.currentState == MultiFloatingState.Exapanded) {
                    MultiFloatingState.Collapsed
                } else {
                    MultiFloatingState.Exapanded
                }
            )
        }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Icon",
                modifier = Modifier.rotate(rotate)
            )
        }
    }
}


@Composable
fun minFab(
    item:minFabItem,
    alpha : Float,
    fabScale  : Float,
    onMinFabItemClicked: (minFabItem) -> Unit
){
    Canvas(modifier = Modifier
        .size(60.dp)
        .clickable(
            onClick = { onMinFabItemClicked.invoke(item) },
            interactionSource = MutableInteractionSource(),
            indication = rememberRipple(
                bounded = false,
                radius = 60.dp,
                color = bluelight
            ),
        ),
    ){
        drawCircle(color = blueBtn,
            radius =fabScale,
            )
        drawImage(
            image = item.icon,
            topLeft = Offset(
                center.x - (item.icon.width / 2),
                center.y - (item.icon.width / 2),
            ),
            alpha = alpha
        )
    }
}