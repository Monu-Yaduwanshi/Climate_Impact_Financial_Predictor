package com.example.climatexpert.MainControl

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.climatexpert.R

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Observe LiveData
    val signUpState by authViewModel.signUpState.observeAsState()

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is AuthViewModel.AuthState.Success -> {
                navController.navigate("home") { popUpTo("SignUpScreen") { inclusive = true } }
                authViewModel.resetSignUpState()
            }
            is AuthViewModel.AuthState.Error -> {
                val errorMessage = (signUpState as AuthViewModel.AuthState.Error).message
                Log.e("SignUpScreen", errorMessage)
                authViewModel.resetSignUpState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Company Logo
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color(0xFFB3E5FC))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.circulartaglogo) // Replace with your logo drawable
                    .build(),
                contentDescription = "Company Logo",
                imageLoader = gifEnabledLoader,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

//        val customColor = Color(0xFF455A64)
//        val buttonTextColor = Color(0xffededed)
//        val linkTextColor = Color(0xFF455A64)
        val customColor = Color(0xFF0D47A1)
        val buttonTextColor = Color(0xffededed)
        val linkTextColor = Color(0xFF0D47A1)

        Text(
            text = "Sign-Up",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // First Name Input Field
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Enter First Name", color = customColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.badge)
                        .build(),
                    contentDescription = "First Name Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = customColor,
                cursorColor = customColor,
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Input Field
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Enter Last Name", color = customColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.badge)
                        .build(),
                    contentDescription = "Last Name Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = customColor,
                cursorColor = customColor,
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email", color = customColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.login)
                        .build(),
                    contentDescription = "Email Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = customColor,
                cursorColor = customColor,
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Password", color = customColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.twostepverification)
                        .build(),
                    contentDescription = "Password Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show", color = customColor)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = customColor,
                cursorColor = customColor,
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign-Up Button
        Button(
            onClick = { authViewModel.signUp(email, password, firstName, lastName, context, navController) },
            colors = ButtonDefaults.buttonColors(
                containerColor = customColor,
                contentColor = buttonTextColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Sign-Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Navigate to Login
        Text(
            text = "Already have an account? Log In",
            color = linkTextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate("login")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Social Login Buttons
        SocialLoginButtons(gifEnabledLoader)

        // Handle Loading and Error States
        when (signUpState) {
            is AuthViewModel.AuthState.Loading -> CircularProgressIndicator()
            is AuthViewModel.AuthState.Error -> {
                Text(
                    text = (signUpState as AuthViewModel.AuthState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            else -> {}
        }
    }
}
