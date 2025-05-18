package com.example.climatexpert.BottomNavBar

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.climatexpert.R
import com.example.climatexpert.network.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownServiceException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Weather", "Alerts")
    val coroutineScope = rememberCoroutineScope()
    val weatherApi = remember { WeatherApiService.create() }
    val apiKey = "0aa9cd139f4a4ba6b55121644250705"

    var selectedCity by remember { mutableStateOf("New York") }
    var showCityDialog by remember { mutableStateOf(false) }
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch weather data when city changes
    LaunchedEffect(selectedCity) {
        isLoading = true
        errorMessage = null
        try {
            weatherData = withContext(Dispatchers.IO) {
                weatherApi.getForecast(apiKey, selectedCity)
            }
        } catch (e: Exception) {
            errorMessage = when (e) {
                is UnknownServiceException -> "Please use HTTPS for API calls"
                is SocketTimeoutException -> "Request timed out"
                is IOException -> "Network error"
                else -> "Failed to fetch weather data: ${e.message}"
            }
            Log.e("WeatherScreen", "API Error", e)
        } finally {
            isLoading = false
        }
    }
//    LaunchedEffect(selectedCity) {
//        isLoading = true
//        errorMessage = null
//        try {
//            weatherData = weatherApi.getForecast(apiKey, selectedCity)
//            isLoading = false
//        } catch (e: Exception) {
//            errorMessage = "Failed to fetch weather data: ${e.message}"
//            isLoading = false
//            Log.e("WeatherScreen", "API Error", e)
//        }
//    }

    // City selection dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Select City") },
            text = {
                Column {
                    listOf("New York", "London", "Tokyo", "Paris", "Sydney", "Mumbai", "Dubai").forEach { city ->
                        TextButton(
                            onClick = {
                                selectedCity = city
                                showCityDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(city, modifier = Modifier.padding(8.dp))
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
                title = { Text("Weather & Alerts", color = Color.White) },
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
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Change City",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFA880D))
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column {
                    // Add the tab row below the top app bar
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color(0xFF0D47A1),
                        contentColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Color.White
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title, color = Color.White) },
                                selected = selectedTab == index,
                                onClick = { selectedTab = index }
                            )
                        }
                    }

                    // Main content area
                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = errorMessage ?: "Error loading data",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                isLoading = true
                                                errorMessage = null
                                                try {
                                                    weatherData = weatherApi.getForecast(apiKey, selectedCity)
                                                    isLoading = false
                                                } catch (e: Exception) {
                                                    errorMessage = "Failed to fetch weather data: ${e.message}"
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                        else -> when (selectedTab) {
                            0 -> weatherData?.let { WeatherContent(it) } ?: run {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No weather data available", color = Color.Gray)
                                }
                            }
                            1 -> weatherData?.let { AlertsContent(it.alerts) } ?: run {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No alert data available", color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


//    Scaffold(
//        topBar = {
//            Column {
//                TopAppBar(
//                    title = { Text("Weather & Alerts - $selectedCity") },
//                    navigationIcon = {
//                        IconButton(onClick = { navController.popBackStack() }) {
//                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                        }
//                    },
//                    actions = {
//                        IconButton(onClick = { showCityDialog = true }) {
//                            Icon(Icons.Default.Edit, contentDescription = "Change City")
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primary,
//                        titleContentColor = MaterialTheme.colorScheme.onPrimary
//                    )
//                )
//
//                TabRow(
//                    selectedTabIndex = selectedTab,
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary,
//                    indicator = { tabPositions ->
//                        TabRowDefaults.Indicator(
//                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
//                            color = MaterialTheme.colorScheme.onPrimary
//                        )
//                    }
//                ) {
//                    tabs.forEachIndexed { index, title ->
//                        Tab(
//                            text = { Text(title) },
//                            selected = selectedTab == index,
//                            onClick = { selectedTab = index }
//                        )
//                    }
//                }
//            }
//        },
//        content = { paddingValues ->
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .background(MaterialTheme.colorScheme.background)
//            ) {
//                when {
//                    isLoading -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
//                    errorMessage != null -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Text(
//                                    text = errorMessage ?: "Error loading data",
//                                    color = MaterialTheme.colorScheme.error,
//                                    modifier = Modifier.padding(16.dp)
//                                )
//                                Button(
//                                    onClick = {
//                                        coroutineScope.launch {
//                                            isLoading = true
//                                            errorMessage = null
//                                            try {
//                                                weatherData = weatherApi.getForecast(apiKey, selectedCity)
//                                                isLoading = false
//                                            } catch (e: Exception) {
//                                                errorMessage = "Failed to fetch weather data: ${e.message}"
//                                                isLoading = false
//                                            }
//                                        }
//                                    }
//                                ) {
//                                    Text("Retry")
//                                }
//                            }
//                        }
//                    }
//                    else -> when (selectedTab) {
//                        0 -> weatherData?.let { WeatherContent(it) } ?: run {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text("No weather data available", color = Color.Gray)
//                            }
//                        }
//                        1 -> weatherData?.let { AlertsContent(it.alerts) } ?: run {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text("No alert data available", color = Color.Gray)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    )
//}

@Composable
fun WeatherContent(weatherData: WeatherResponse) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CurrentWeatherCard(weatherData.location, weatherData.current)
        }
        weatherData.forecast?.forecastday?.firstOrNull()?.let { forecastDay ->
            item {
                ForecastCard(forecastDay.day)
            }
        }
        item {
            DisasterRiskCard(weatherData)
        }
    }
}

@Composable
fun CurrentWeatherCard(location: Location, current: CurrentWeather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${location.name}, ${location.country}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Last updated: ${current.last_updated}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https:${current.condition.icon}",
                        placeholder = painterResource(R.drawable.logo)
                    ),
                    contentDescription = current.condition.text,
                    modifier = Modifier.size(64.dp)
                )

                Column {
                    Text(
                        text = "${current.temp_c}°C",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(current.condition.text)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetailItem("Wind", "${current.wind_kph} km/h")
                WeatherDetailItem("Humidity", "${current.humidity}%")
                WeatherDetailItem("Precip", "${current.precip_mm}mm")
                WeatherDetailItem("UV", current.uv.toString())
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetailItem("Feels Like", "${current.feelslike_c}°C")
                WeatherDetailItem("Pressure", "${current.pressure_mb} mb")
                WeatherDetailItem("Visibility", "${current.vis_km} km")
            }
        }
    }
}

@Composable
fun ForecastCard(forecast: DayForecast) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Today's Forecast",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetailItem("High", "${forecast.maxtemp_c}°C")
                WeatherDetailItem("Low", "${forecast.mintemp_c}°C")
                WeatherDetailItem("Rain", "${forecast.totalprecip_mm}mm")
                WeatherDetailItem("Chance", "${forecast.daily_chance_of_rain}%")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https:${forecast.condition.icon}"),
                    contentDescription = forecast.condition.text,
                    modifier = Modifier.size(48.dp)
                )
                Text(forecast.condition.text)
            }
        }
    }
}

@Composable
fun DisasterRiskCard(weatherData: WeatherResponse) {
    val riskLevel = remember(weatherData) {
        calculateRiskLevel(weatherData)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (riskLevel) {
                "High" -> Color(0xFFFFEBEE)
                "Moderate" -> Color(0xFFFFF3E0)
                else -> Color(0xFFE8F5E9)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Disaster Risk Assessment",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = when (riskLevel) {
                    "High" -> "⚠️ High risk of natural disasters"
                    "Moderate" -> "⚠️ Moderate risk of natural disasters"
                    else -> "✅ Low risk of natural disasters"
                },
                color = when (riskLevel) {
                    "High" -> Color.Red
                    "Moderate" -> Color(0xFFFF6D00)
                    else -> Color(0xFF388E3C)
                }
            )

            Text(
                text = when {
                    weatherData.alerts?.alert?.isNotEmpty() == true ->
                        "Active weather alerts in your area. ${weatherData.alerts.alert.size} alert(s) issued."
                    riskLevel == "High" ->
                        "Conditions indicate potential for severe weather events."
                    riskLevel == "Moderate" ->
                        "Some weather conditions may pose risks."
                    else ->
                        "No significant flood, storm or heat risks detected in your area"
                },
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun AlertsContent(alerts: Alerts?) {
    if (alerts?.alert.isNullOrEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No active alerts", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(alerts?.alert ?: emptyList()) { alert ->
                AlertCard(alert)
            }
        }
    }
}

@Composable
fun AlertCard(alert: Alert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (alert.severity.lowercase()) {
                "extreme", "severe" -> Color(0xFFFFEBEE)
                "moderate", "high" -> Color(0xFFFFF3E0)
                else -> Color(0xFFE8F5E9)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Alert",
                    tint = when (alert.severity.lowercase()) {
                        "extreme", "severe" -> Color.Red
                        "moderate", "high" -> Color(0xFFFF6D00)
                        else -> Color.Gray
                    }
                )
                Text(
                    text = alert.headline,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Text(
                text = "Severity: ${alert.severity}",
                color = when (alert.severity.lowercase()) {
                    "extreme", "severe" -> Color.Red
                    "moderate", "high" -> Color(0xFFFF6D00)
                    else -> Color.Gray
                }
            )

            Text(
                text = "Areas: ${alert.areas}",
                color = Color.Gray
            )

            Text(
                text = "Effective: ${alert.effective}",
                color = Color.Gray
            )

            Text(
                text = "Expires: ${alert.expires}",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = alert.desc,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Instructions: ${alert.instruction}",
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun calculateRiskLevel(weatherData: WeatherResponse): String {
    val current = weatherData.current
    val alertsCount = weatherData.alerts?.alert?.size ?: 0

    return when {
        alertsCount > 0 -> "High"
        current.precip_mm > 50 || current.wind_kph > 50 || current.temp_c > 35 || current.temp_c < -10 -> "Moderate"
        else -> "Low"
    }
}