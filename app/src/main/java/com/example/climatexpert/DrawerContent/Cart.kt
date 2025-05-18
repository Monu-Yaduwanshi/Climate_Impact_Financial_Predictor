package com.example.climatexpert.DrawerContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<InsuranceItem>,
    onRemoveItem: (InsuranceItem) -> Unit,
    onProceedToPayment: () -> Unit,
    navController: NavHostController,
    firebaseRepository: FirebaseRepository = FirebaseRepository()
) {
    val cartItems = remember { mutableStateListOf<InsuranceItem>() }
    val totalAmount = remember { derivedStateOf { cartItems.sumOf { it.price } } }

    // Load cart from Firebase
    LaunchedEffect(Unit) {
        firebaseRepository.getUserCartRef().addValueEventListener( object : ValueEventListener {
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
    }

    fun onRemoveItem(item: InsuranceItem) {
        cartItems.remove(item)
        firebaseRepository.getUserCartRef().setValue(UserCart(
            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            items = cartItems
        ))
    }

    fun onProceedToPayment() {
        navController.navigate("payment/${totalAmount.value}")
    }




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Insurance Cart",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFA880D)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F9FF))
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            contentDescription = "Empty Cart",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Your cart is empty",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            "Add climate protection plans to get started",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            onRemove = { onRemoveItem(item) }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total Premium:",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF0D47A1)
                        )
                        Text(
                            "₹${cartItems.sumOf { it.price }}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFFF6D00)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onProceedToPayment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D47A1)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Text(
                            "Proceed to Payment",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
private fun updateCartInFirebase(
    items: List<InsuranceItem>,
    firebaseRepository: FirebaseRepository
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val userCart = UserCart(userId, items)
    firebaseRepository.getUserCartRef().setValue(userCart)
}

@Composable
fun CartItemCard(item: InsuranceItem, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF0D47A1)
                    )
                    Text(
                        "₹${item.price}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFF6D00),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Remove",
                        tint = Color.Red
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Coverage: ${item.coverage}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                item.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Waiting period: ${item.waitingPeriod} days",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}