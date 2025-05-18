package com.example.climatexpert.Navigations
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.climatexpert.BottomNavBar.*
import com.example.climatexpert.DrawerContent.InsuranceItem
import com.example.climatexpert.DrawerContent.*
import com.example.climatexpert.MainControl.AuthViewModel
import com.example.climatexpert.MainControl.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val cartItems = remember { mutableStateListOf<InsuranceItem>() }
    val firebaseRepository = remember { FirebaseRepository() }
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Auth Flow
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("signup") { SignUpScreen(navController, authViewModel) }

        // Main App Screens
        composable("home") { HomeScreen(navController) }
        composable("climatexpert") { ClimateXpertScreen(navController) }
        composable("checkclimate") { CheckClimate(navController) }
        composable("weather") { WeatherScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
      //  composable("account") { AccountScreen(navController) }
        composable("aboutus") { AboutScreen(navController) }
        composable("logout") { LogoutScreen(navController) }
        composable("notification") { NotificationScreen(navController) }

        // Insurance Flow
        composable("insuranceList") {
            InsuranceListScreen(
                navController = navController,
                firebaseRepository = firebaseRepository,
                onAddToCart = { item ->
                    firebaseRepository.getUserCartRef().get().addOnSuccessListener { snapshot ->
                        val currentCart = snapshot.getValue(UserCart::class.java) ?:
                        UserCart(userId = FirebaseAuth.getInstance().currentUser?.uid ?: "")
                        val updatedItems = currentCart.items.toMutableList().apply { add(item) }
                        firebaseRepository.getUserCartRef().setValue(currentCart.copy(items = updatedItems))
                    }
                }
            )
        }
        composable("cart") {
            CartScreen(
                navController = navController,
                firebaseRepository = firebaseRepository,
                cartItems = emptyList(), // Loaded from Firebase inside CartScreen
                onRemoveItem = { item ->
                    firebaseRepository.getUserCartRef().get().addOnSuccessListener { snapshot ->
                        val currentCart = snapshot.getValue(UserCart::class.java) ?: return@addOnSuccessListener
                        val updatedItems = currentCart.items.toMutableList().apply { remove(item) }
                        firebaseRepository.getUserCartRef().setValue(currentCart.copy(items = updatedItems))
                    }
                },
                onProceedToPayment = {
                    firebaseRepository.getUserCartRef().get().addOnSuccessListener { snapshot ->
                        val total = snapshot.getValue(UserCart::class.java)?.items?.sumOf { it.price } ?: 0
                        navController.navigate("payment/$total")
                    }
                }
            )
        }
        composable("payment/{total}") { backStackEntry ->
            val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0
            PaymentsScreen(
                navController = navController,
                totalAmount = total,
                firebaseRepository = firebaseRepository,
                onPaymentComplete = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@PaymentsScreen
                    firebaseRepository.getUserCartRef().get().addOnSuccessListener { snapshot ->
                        val cart = snapshot.getValue(UserCart::class.java) ?: return@addOnSuccessListener
                        val orderId = firebaseRepository.getOrdersRef().push().key ?: return@addOnSuccessListener
                        val order = Order(
                            orderId = orderId,
                            userId = userId,
                            items = cart.items,
                            totalAmount = total,
                            paymentMethod = "UPI"
                        )
                        firebaseRepository.getOrdersRef().child(orderId).setValue(order)
                        firebaseRepository.getUserCartRef().removeValue()
                        navController.popBackStack("insuranceList", inclusive = false)
                    }
                }
            )
        }
        composable("account") {
            AccountScreen(
                navController = navController,
                firebaseRepository = firebaseRepository
            )
        }


        }

    }

