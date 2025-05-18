package com.example.climatexpert.BottomNavBar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.climatexpert.ML.ClimateModelHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckClimate(navController: NavController) {
    val context = LocalContext.current
    val modelHelper = remember { ClimateModelHelper(context) }

    var temperature by remember { mutableStateOf("") }
    var rainfall by remember { mutableStateOf("") }
    var windSpeed by remember { mutableStateOf("") }
    var economicLoss by remember { mutableStateOf("") }
    var pastDisaster by remember { mutableStateOf(false) }
    var prediction by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Climate Check", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // ✅ Back Button
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFA880D)) // Dark Blue
            )
        },
        bottomBar = { BottomNavigationBar(navController) } // ✅ Bottom Navbar added
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFB3E5FC))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter Climate Data for Prediction",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = temperature,
                onValueChange = { temperature = it },
                label = { Text("Temperature (°C)") },
                placeholder = { Text("e.g., 30.5") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rainfall,
                onValueChange = { rainfall = it },
                label = { Text("Rainfall (mm)") },
                placeholder = { Text("e.g., 50.0") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = windSpeed,
                onValueChange = { windSpeed = it },
                label = { Text("Wind Speed (km/h)") },
                placeholder = { Text("e.g., 12.3") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = economicLoss,
                onValueChange = { economicLoss = it },
                label = { Text("Estimated Economic Loss (USD)") },
                placeholder = { Text("e.g., 100000") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Past disaster occurred?")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = pastDisaster,
                    onCheckedChange = { pastDisaster = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    errorMessage = ""

                    val temp = temperature.toFloatOrNull()
                    val rain = rainfall.toFloatOrNull()
                    val wind = windSpeed.toFloatOrNull()
                    val loss = economicLoss.toFloatOrNull()
                    val past = if (pastDisaster) 1f else 0f

                    if (temp == null || rain == null || wind == null || loss == null) {
                        errorMessage = "❌ Please enter valid numbers!"
                    } else if (temp !in -50f..50f || rain !in 0f..1000f || wind !in 0f..200f || loss < 0f) {
                        errorMessage = """
                ❌ Enter values within valid ranges:
                Temperature: -50 to 100°C
                Rainfall: 0 to 1000 mm
                Wind Speed: 0 to 200 km/h
                Economic Loss: Positive value
            """.trimIndent()
                    } else {
                        val inputData = floatArrayOf(temp, rain, wind, loss, past)

                        try {
                            prediction = modelHelper.predict(inputData)
                        } catch (e: Exception) {
                            Log.e("ClimateModel", "Prediction Error: ${e.message}")
                            errorMessage = "❌ Model encountered an error! Check log."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Predict")
            }
//            Button(
//                onClick = {
//                    errorMessage = ""
//
//                    val temp = temperature.toFloatOrNull()
//                    val rain = rainfall.toFloatOrNull()
//                    val wind = windSpeed.toFloatOrNull()
//                    val loss = economicLoss.toFloatOrNull()
//                    val past = if (pastDisaster) 1f else 0f
//
//                    if (temp == null || rain == null || wind == null || loss == null) {
//                        errorMessage = "❌ Please enter valid numbers!"
//                    } else {
//                        val inputData = floatArrayOf(temp, rain, wind, loss, past)
//
//                        try {
//                            prediction = modelHelper.predict(inputData)
//                        } catch (e: Exception) {
//                            Log.e("ClimateModel", "Prediction Error: ${e.message}")
//                            errorMessage = "❌ Model encountered an error! Check log."
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Predict")
//            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, fontSize = 16.sp, color = Color.Red)
            }

            if (prediction.isNotEmpty()) {
                Text(text = prediction, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}
