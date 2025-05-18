package com.example.climatexpert.MainControl
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.climatexpert.MainControl.AuthViewModel
import com.example.climatexpert.MainControl.AuthViewModelFactory
import com.example.climatexpert.Navigations.AppNavigation
import com.example.climatexpert.ui.theme.ClimateXpertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClimateXpertTheme {
                // ✅ Fix: Use ViewModelProvider with application context
                val authViewModel: AuthViewModel = ViewModelProvider(
                    this,
                    AuthViewModelFactory(application)
                )[AuthViewModel::class.java]

                AppNavigation(authViewModel)
            }
        }
    }
}
