package com.example.climatexpert.DrawerContent

import androidx.compose.foundation.background
import androidx.compose.ui.Alignment

//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.climatexpert.BottomNavBar.BottomNavigationBar
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InsuranceScreen(navController: NavController) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Insurance", color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) { // ✅ Back Button
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1)) // Dark Blue
//            )
//        },
//     //   bottomBar = { BottomNavigationBar(navController) } // ✅ Bottom Navbar added
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "Insurance Screen", fontSize = 24.sp)
//        }
//    }
//}
// InsuranceScreen.kt
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Error
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InsuranceScreen(navController: NavController) {
//    val viewModel: InsuranceViewModel = viewModel(
//        factory = object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return InsuranceViewModel(InsuranceRepository()) as T
//            }
//        }
//    )
//
//    val insuranceResponse by viewModel.insuranceResponse.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.fetchPolicyDocument()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Insurance Policy", color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF0D47A1)
//                )
//            )
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            when {
//                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
//                error != null -> ErrorScreen(
//                    error = error!!,
//                    onRetry = { viewModel.fetchPolicyDocument() },
//                    modifier = Modifier.align(Alignment.Center)
//                )
//                insuranceResponse != null -> PolicyDetails(
//                    insuranceResponse = insuranceResponse!!,
//                    modifier = Modifier.fillMaxSize())
//                else -> Text("No data available", Modifier.align(Alignment.Center))
//            }
//        }
//    }
//}
//
//@Composable
//fun PolicyDetails(
//    insuranceResponse: InsuranceResponse,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier.padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        item {
//            Text(
//                text = "Policy Details",
//                style = MaterialTheme.typography.headlineMedium
//            )
//            Divider()
//        }
//
//        item {
//            Column {
//                Text(
//                    text = "Policy Number:",
//                    fontWeight = FontWeight.Bold
//                )
//                Text(text = insuranceResponse.certificateParameters.POLNO)
//            }
//        }
//
//        item {
//            Column {
//                Text(
//                    text = "Inception Date:",
//                    fontWeight = FontWeight.Bold
//                )
//                Text(text = insuranceResponse.certificateParameters.INCPDT)
//            }
//        }
//
//        item {
//            Button(
//                onClick = { /* Handle download */ },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF0D47A1)
//                )
//            ) {
//                Text("Download Policy Document (${insuranceResponse.format.uppercase()})")
//            }
//        }
//    }
//}
//@Composable
//fun ErrorScreen(
//    error: String,
//    onRetry: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.Error,
//            contentDescription = "Error",
//            tint = MaterialTheme.colorScheme.error,
//            modifier = Modifier.size(48.dp)
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Text(
//            text = "Request Failed",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.error
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        Text(
//            text = error,
//            style = MaterialTheme.typography.bodyMedium,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(Modifier.height(24.dp))
//
//        Button(
//            onClick = onRetry,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.primary
//            )
//        ) {
//            Text("Try Again")
//        }
//    }
//}


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@Composable
fun InsuranceListScreen(
    navController: NavHostController,
    onAddToCart: (InsuranceItem) -> Unit,
    firebaseRepository: FirebaseRepository
){
    val allItems = remember { InsuranceRepository.getBusinessInsuranceItems() }
    val displayedItems = remember { mutableStateListOf<InsuranceItem>() }
    val cartItems = remember { mutableStateListOf<InsuranceItem>() }

    // Load first 5 items
    LaunchedEffect(Unit) {
        displayedItems.addAll(allItems.take(5))
    }
    val listState = rememberLazyListState()
    LaunchedEffect(listState.layoutInfo) {
        if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == displayedItems.size - 1) {
            val nextItems = allItems.drop(displayedItems.size).take(5)
            displayedItems.addAll(nextItems)
        }
    }
    // Load cart
    firebaseRepository.getUserCartRef().addValueEventListener(  object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val cart = snapshot.getValue(UserCart::class.java)
            cart?.items?.let {
                cartItems.clear()
                cartItems.addAll(it)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })




    fun onAddToCart(item: InsuranceItem) {
        cartItems.add(item)
        firebaseRepository.getUserCartRef().setValue(UserCart(
            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            items = cartItems
        ))
    }
    Scaffold(
        topBar = {
            ClimateRiskTopBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cart") },
                containerColor = Color(0xFFFA880D),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.ShoppingCart, "Cart")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F9FF))
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(displayedItems) { item ->
                    ClimateRiskInsuranceCard(
                        item = item,
                        onAddToCart = {
                            onAddToCart(item)
                            // Optional: Show snackbar confirmation
                        }
                    )
                }
            }
        }
    }
}
//    // Pagination
//    val listState = rememberLazyListState()
//    LaunchedEffect(listState.layoutInfo) {
//        if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == displayedItems.size - 1) {
//            val nextItems = allItems.drop(displayedItems.size).take(5)
//            displayedItems.addAll(nextItems)
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            ClimateRiskTopBar(navController)
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(Color(0xFFF5F9FF))  // Very light blue background
//        ) {
//            LazyColumn(
//                state = listState,
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                contentPadding = PaddingValues(16.dp)
//            ) {
//                items(displayedItems) { item ->
//                    ClimateRiskInsuranceCard(
//                        item = item,
//                        onAddToCart = {
//                            cartItems.add(item)
//                            navController.navigate("cart")
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimateRiskTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                "Business Insurance",
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFA880D)  // Dark blue
        )
    )
}

@Composable
fun ClimateRiskInsuranceCard(
    item: InsuranceItem,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Climate risk indicator
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = when(item.climateRisk) {
                                "Flooding" -> Color(0xFF64B5F6)
                                "Wildfires" -> Color(0xFFFF8A65)
                                "Hurricanes" -> Color(0xFF4FC3F7)
                                else -> Color(0xFF90A4AE)
                            },
                            shape = CircleShape
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.climateRisk.take(2),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF0D47A1)
                    )
                    Text(
                        "Covers: ${item.coverage}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                item.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "₹${item.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFFFF6D00)  // Orange
                )

                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D47A1)
                    )
                ) {
                    Text("Add to Cart")
                }
            }

            Text(
                "Waiting period: ${item.waitingPeriod} days",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}