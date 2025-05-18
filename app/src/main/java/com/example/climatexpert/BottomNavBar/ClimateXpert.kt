package com.example.climatexpert.BottomNavBar
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.example.climatexpert.network.WeatherApiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimateXpertScreen(navController: NavController) {
    val context = LocalContext.current
    val modelHelper = remember { ClimateModelHelper(context) }
    val weatherApi = remember { WeatherApiService.create() }
    val apiKey = "0aa9cd139f4a4ba6b55121644250705"

    var selectedCity by remember { mutableStateOf("New York") }
    var showCityDialog by remember { mutableStateOf(false) }
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var prediction by remember { mutableStateOf("") }

    // Fetch weather data when city changes
    LaunchedEffect(selectedCity) {
        isLoading = true
        errorMessage = ""
        try {
            weatherData = weatherApi.getCurrentWeather(apiKey, selectedCity)
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Failed to fetch weather data: ${e.message}"
            isLoading = false
        }
    }

    // City selection dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Select City") },
            text = {
                Column {
                    listOf("New York", "London", "Tokyo", "Paris", "Sydney").forEach { city ->
                        TextButton(
                            onClick = {
                                selectedCity = city
                                showCityDialog = false
                            }
                        ) {
                            Text(city)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ClimateXpert AI - $selectedCity", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showCityDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Change City", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFA880D)))
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFB3E5FC))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(errorMessage, color = Color.Red)
                }
            } else {
                weatherData?.let { data ->
                    // Auto-fill fields with current weather data
                    val current = data.current

                    Button(
                        onClick = {
                            errorMessage = ""
                            try {
                                val inputData = floatArrayOf(
                                    current.temp_c.toFloat(),
                                    current.precip_mm.toFloat(),
                                    current.wind_kph.toFloat(),
                                    // Estimate economic loss based on conditions
                                    estimateEconomicLoss(current).toFloat(),
                                    // Assume past disaster if there are active alerts
                                    if (data.alerts?.alert?.isNotEmpty() == true) 1f else 0f
                                )
                                prediction = modelHelper.predict(inputData)
                            } catch (e: Exception) {
                                errorMessage = "❌ Prediction error: ${e.message}"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Predict Using Current Weather")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (prediction.isNotEmpty()) {
                        Text(
                            text = prediction,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    // Show current weather summary
                    CurrentWeatherSummary(current)
                }
            }
        }
    }
}

// Helper function to estimate economic loss based on weather conditions
fun estimateEconomicLoss(current: CurrentWeather): Double {
    return when {
        current.precip_mm > 50 -> 100000.0 // Heavy rain
        current.wind_kph > 50 -> 150000.0 // Strong winds
        current.temp_c > 35 -> 80000.0 // Heat wave
        current.temp_c < -10 -> 90000.0 // Cold wave
        else -> 5000.0 // Normal conditions
    }
}

@Composable
fun CurrentWeatherSummary(current: CurrentWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Current Weather", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Temperature: ${current.temp_c}°C")
            Text("Condition: ${current.condition.text}")
            Text("Wind: ${current.wind_kph} km/h")
            Text("Precipitation: ${current.precip_mm} mm")
            Text("Humidity: ${current.humidity}%")
        }
    }
}