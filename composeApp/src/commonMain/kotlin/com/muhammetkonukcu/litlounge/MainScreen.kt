package com.muhammetkonukcu.litlounge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.muhammetkonukcu.litlounge.lang.AppLang
import com.muhammetkonukcu.litlounge.lang.rememberAppLocale
import com.muhammetkonukcu.litlounge.model.BottomNavModel
import com.muhammetkonukcu.litlounge.theme.AppTheme
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.clock_clockwise
import litlounge.composeapp.generated.resources.clock_countdown_fill
import litlounge.composeapp.generated.resources.history
import litlounge.composeapp.generated.resources.home
import litlounge.composeapp.generated.resources.ph_house
import litlounge.composeapp.generated.resources.ph_house_fill
import litlounge.composeapp.generated.resources.profile
import litlounge.composeapp.generated.resources.user_gear
import litlounge.composeapp.generated.resources.user_gear_fill
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalAppLocalization = compositionLocalOf {
    AppLang.English
}

@Composable
@Preview
fun MainScreen() {
    val currentLanguage = rememberAppLocale()
    CompositionLocalProvider(LocalAppLocalization provides currentLanguage) {
        AppTheme {
            Surface(
                modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = {
                        if (currentRoute in listOf("Home", "History", "Profile")) {
                            BottomNavigation(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Home",
                    ) {
                        composable("Home") {
                            HomeScreen(
                                navController = navController,
                                innerPadding = innerPadding
                            )
                        }
                        composable("History") {
                            HistoryScreen(
                                navController = navController,
                                innerPadding = innerPadding
                            )
                        }
                        composable("Profile") {
                            ProfileScreen(
                                navController = navController,
                                innerPadding = innerPadding
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SetNavItems(): List<BottomNavModel> {
    val navigationItems = listOf(
        BottomNavModel(
            route = "Home",
            title = stringResource(Res.string.home),
            selectedIcon = painterResource(Res.drawable.ph_house_fill),
            unselectedIcon = painterResource(Res.drawable.ph_house),
            hasNews = false
        ),
        BottomNavModel(
            route = "History",
            title = stringResource(Res.string.history),
            selectedIcon = painterResource(Res.drawable.clock_countdown_fill),
            unselectedIcon = painterResource(Res.drawable.clock_clockwise),
            hasNews = false
        ),
        BottomNavModel(
            route = "Profile",
            title = stringResource(Res.string.profile),
            selectedIcon = painterResource(Res.drawable.user_gear_fill),
            unselectedIcon = painterResource(Res.drawable.user_gear),
            hasNews = false
        ),
    )
    return navigationItems
}

@Composable
fun BottomNavigation(navController: NavController) {
    val navigationItems = SetNavItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry
        ?.destination
        ?.route

    NavigationBar(
        modifier = Modifier.height(50.dp),
        tonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        navigationItems.forEachIndexed { index, item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                alwaysShowLabel = true,
                colors = NavigationBarItemColors(
                    selectedIndicatorColor = Color.Unspecified,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    disabledIconColor = MaterialTheme.colorScheme.tertiary,
                    disabledTextColor = MaterialTheme.colorScheme.tertiary
                ),
                label = {
                    Text(
                        text = item.title,
                        style = if (selected) MaterialTheme.typography.labelMedium
                        else MaterialTheme.typography.labelSmall
                    )
                },
                icon = {
                    BadgedBox(badge = { if (item.hasNews) Badge() }) {
                        Icon(
                            painter = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                },
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().route ?: "") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}