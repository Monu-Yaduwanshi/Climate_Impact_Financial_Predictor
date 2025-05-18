package com.example.climatexpert.MainControl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.climatexpert.R
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)), // ✅ Light Blue Background
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ Heading
        Text(
            text = "ClimateXpert",
            fontSize = 32.sp, // Bigger heading
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1) // Dark Blue
        )

        // ✅ Tagline
        Text(
            text = "Turning Climate Challenges into Opportunities",
            fontSize = 16.sp, // Slightly smaller tagline
            color = Color(0xFFFA880D ), // Slightly darker than heading
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ✅ App Logo
        Icon(
            painter = painterResource(id = R.drawable.slogos),
            contentDescription = "App Logo",
            modifier = Modifier.size(250.dp), // ✅ Increased Logo Size
            tint = Color.Unspecified // ✅ Prevents black tint on logo
        )

        // ✅ Navigate to the next screen after delay
        LaunchedEffect(Unit) {
            delay(1500) // 1.5-second delay

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}
