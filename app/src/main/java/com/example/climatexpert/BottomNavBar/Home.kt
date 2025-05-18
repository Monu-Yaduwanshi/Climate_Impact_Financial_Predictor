package com.example.climatexpert.BottomNavBar
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import com.example.climatexpert.R
import kotlinx.coroutines.delay
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scaffoldState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 10 })

    ModalNavigationDrawer(
        drawerState = scaffoldState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
                    .background(Color(0xFFB3E5FF))
                    .pointerInput(Unit) {
                        detectTapGestures { scope.launch { scaffoldState.close() } }
                    }
            ) {
                DrawerMenu(navController) { scope.launch { scaffoldState.close() } }
            }
        },
        gesturesEnabled = false
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB3E5FC))
                    .padding(paddingValues)
            ) {
                // 🌟 Top App Bar (Decorated)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1e3d58)) // Light Orange
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { scope.launch { scaffoldState.open() } }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFFFFA500),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(id = R.drawable.slogos),
                            contentDescription = "Logo",
                            modifier = Modifier.size(50.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ClimateXpert",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9800)
                            )
                            Text(
                                text = "Turning Climate Challenges into Opportunities",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF057dcd)
                            )
                        }

                        Row {
                            IconButton(onClick = { navController.navigate("notification") }) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            IconButton(onClick = { navController.navigate("cart") }) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }

                // 🌈 Auto-Moving Weather Image Slider
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1000L)
                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % 10)
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val images = listOf(
                        R.drawable.rainyday,
                        R.drawable.snow,
                        R.drawable.storm,
                        R.drawable.sun,
                        R.drawable.snow,
                        R.drawable.snowy,
                        R.drawable.sun1,
                        R.drawable.sunny,
                        R.drawable.tornado,
                        R.drawable.rainbow
                    )
                    Image(
                        painter = painterResource(id = images[page]),
                        contentDescription = "Weather Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(navController: NavController) {
//    val scaffoldState = rememberDrawerState(DrawerValue.Closed) // Default closed
//    val scope = rememberCoroutineScope()
//    val pagerState = rememberPagerState(pageCount = { 10 }) // 10 images
//
//    ModalNavigationDrawer(
//        drawerState = scaffoldState,
//        drawerContent = {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(0.8f) // ✅ Drawer opens to **half the screen width**
//                    .fillMaxHeight() // Full height
//                    .background(Color(0xFFB3E5FF)) // Light Blue Background
//                    .pointerInput(Unit) { // ✅ Close drawer when clicking outside
//                        detectTapGestures { scope.launch { scaffoldState.close() } }
//                    }
//            ) {
//                DrawerMenu(navController) { scope.launch { scaffoldState.close() } }
//            }
//        },
//        gesturesEnabled = false // ✅ Prevents accidental drawer opening
//    ) {
//        Scaffold(
//            bottomBar = { BottomNavigationBar(navController) }
//        ) { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xFFB3E5FC)) // Light Blue Background
//                    .padding(paddingValues)
//            ) {
//                // ✅ Header (Clickable Icon to Open Drawer)
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.Yellow) // 🟡 Yellow background just for this top section
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = { scope.launch { scaffoldState.open() } }) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Menu",
//                            tint = Color(0xFFFFA500), // 🟠 Orange icon
//                            modifier = Modifier.size(32.dp)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Image(
//                        painter = painterResource(id = R.drawable.slogos),
//                        contentDescription = "User Icon",
//                        modifier = Modifier.size(55.dp)
//                    )
//
//                    Spacer(modifier = Modifier.width(4.dp))
//
//                    Column {
//                        Text(
//                            text = "ClimateXpert",
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFFFA500) // 🟠 Orange
//                        )
//                        Text(
//                            text = "Turning Climate Challenges into Opportunities",
//                            fontSize = 15.sp,
//                            color = Color.Blue
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.weight(1f)) // ⏩ Pushes next icons to right
//
//                    // 🔔 Notification Icon
//                    IconButton(onClick = { navController.navigate("notification") }) {
//                        Icon(
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = "Notifications",
//                            tint = Color(0xFFFFA500),
//                            modifier = Modifier.size(28.dp)
//                        )
//                    }
//
//                    // 🛒 Cart Icon
//                    IconButton(onClick = { navController.navigate("cart") }) {
//                        Icon(
//                            imageVector = Icons.Default.ShoppingCart,
//                            contentDescription = "Cart",
//                            tint = Color(0xFFFFA500),
//                            modifier = Modifier.size(28.dp)
//                        )
//                    }
//                }
//
//
//                // ✅ Auto-Moving Weather Image Slider
//                LaunchedEffect(Unit) {
//                    while (true) {
//                        delay(1000L) // Move to next image every 1 second
//                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % 10)
//                    }
//                }
//
//                HorizontalPager(
//                    state = pagerState,
//                    modifier = Modifier.fillMaxSize()
//                ) { page ->
//                    val images = listOf(
//                        R.drawable.rainyday,
//                        R.drawable.snow,
//                        R.drawable.storm,
//                        R.drawable.sun,
//                        R.drawable.snow,
//                        R.drawable.snowy,
//                        R.drawable.sun1,
//                        R.drawable.sunny,
//                        R.drawable.tornado,
//                        R.drawable.rainbow
//                    )
//                    Image(
//                        painter = painterResource(id = images[page]),
//                        contentDescription = "Weather Image",
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            }
//        }
//    }
//}

// ✅ Bottom Navigation Bar (Blue Background, Dark Blue Icons)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.mhome, "Home"),
        BottomNavItem("climatexpert", R.drawable.mexpert, "ClimateXpert"),
        BottomNavItem("weather", R.drawable.mclimate, "Weather"),
        BottomNavItem("checkclimate", R.drawable.climatechanges, "Check Climate")
    )

    NavigationBar(containerColor = Color(0xFF1976D2)) { // Blue Background
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = Color(0xFF0D47A1), // Dark Blue Icon
                        modifier = Modifier.size(30.dp) // Same Size for All Icons
                    )
                },
                label = { Text(text = item.title, color = Color.White) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: Int, val title: String)

// ✅ Drawer Navigation (Light Blue Background, Same Size Icons)
@Composable
fun DrawerMenu(navController: NavController, closeDrawer: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFB3E5FC)).padding(16.dp) // Light Blue Background
    ) {
        // ✅ Logo with Tagline
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.slogos),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp).background(Color.White, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Turning Climate Challenges into Opportunities",
                    fontSize = 14.sp,
                    color = Color(0xFFFFA500) // Orange Color
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Drawer Items
        DrawerItem("Profile", R.drawable.bookkeeping, navController, "profile", closeDrawer)
        DrawerItem("Account", R.drawable.accountant, navController, "account", closeDrawer)
        DrawerItem("Insurance", R.drawable.lifeinsurance, navController, "insuranceList", closeDrawer)
        DrawerItem("About Us", R.drawable.mabout, navController, "aboutus", closeDrawer)
        DrawerItem("Logout", R.drawable.mexit, navController, "logout", closeDrawer)
    }
}

// ✅ Drawer Menu Items (Same Size, Light Blue Background)
@Composable
fun DrawerItem(title: String, icon: Int, navController: NavController, route: String, closeDrawer: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp).clickable {
            navController.navigate(route)
            closeDrawer()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = Color(0xFF0D47A1), // Dark Blue Icon
            modifier = Modifier.size(30.dp).background(Color(0xFFB3E5FC), CircleShape) // Light Blue Background
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 18.sp, color = Color(0xFF0D47A1)) // Dark Blue Text
    }
}
