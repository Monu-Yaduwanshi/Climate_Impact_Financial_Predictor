package com.example.climatexpert.DrawerContent

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.climatexpert.R
import com.example.climatexpert.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ProfileAuthViewModel = viewModel(
        factory = ProfileAuthViewModelFactory(
            context.applicationContext as Application
        )
    )
  //  val viewModel: ProfileAuthViewModel = viewModel(factory = ProfileAuthViewModelFactory(context as Application))
    val profileData by viewModel.profileData.observeAsState()
    val uploadStatus by viewModel.uploadStatus.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var isEditing by remember { mutableStateOf(false) }
    var tempProfile by remember { mutableStateOf(profileData ?: ProfileAuthViewModel.UserProfile()) }




    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImageToCloudinary(it) }
    }

    LaunchedEffect(profileData) {
        profileData?.let { tempProfile = it }
    }

    Scaffold(
        topBar = {
            GradientTopAppBar(
                title = "My Profile",
                navController = navController,
                isEditing = isEditing,
                onEditClick = { isEditing = true },
                onSaveClick = {
                    viewModel.saveUserProfile(tempProfile)
                    isEditing = false
                },
                onCancelClick = {
                    isEditing = false
                    tempProfile = profileData ?: ProfileAuthViewModel.UserProfile()
                }
            )
        },
        containerColor = Color(0xFF43b0f1)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Message
                uploadStatus?.let { status ->
                    Text(
                        text = status,
                        color = if (status.contains("success", ignoreCase = true)) SuccessGreen else ErrorRed,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Profile Picture
                ProfileImageSection(
                    imageUrl = tempProfile.profileImageUrl,
                    isEditing = isEditing,
                    onImageClick = { if (isEditing) imagePicker.launch("image/*") }
                )

                // Profile Form
                ProfileFormSection(
                    tempProfile = tempProfile,
                    isEditing = isEditing,
                    onProfileChange = { tempProfile = it }
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                    color = Blue500
                )
            }
        }
    }
}

@Composable
private fun ProfileImageSection(
    imageUrl: String,
    isEditing: Boolean,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(24.dp)
            .size(150.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(colors = listOf(Blue400, Blue700)),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(enabled = isEditing, onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Placeholder",
                modifier = Modifier.size(60.dp),
                tint = Blue200
            )
        }

        if (isEditing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Photo",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ProfileFormSection(
    tempProfile: ProfileAuthViewModel.UserProfile,
    isEditing: Boolean,
    onProfileChange: (ProfileAuthViewModel.UserProfile) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name Section
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tempProfile.firstName,
                    onValueChange = { onProfileChange(tempProfile.copy(firstName = it)) },
                    label = { Text("First Name") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditing,
                    colors = blueTextFieldColors()
                )

                OutlinedTextField(
                    value = tempProfile.lastName,
                    onValueChange = { onProfileChange(tempProfile.copy(lastName = it)) },
                    label = { Text("Last Name") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditing,
                    colors = blueTextFieldColors()
                )
            }

            // Email (Read-only)
            OutlinedTextField(
                value = tempProfile.email,
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = disabledTextFieldColors()
            )

            // Contact Section
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tempProfile.mobileNumber,
                    onValueChange = { onProfileChange(tempProfile.copy(mobileNumber = it)) },
                    label = { Text("Mobile") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditing,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = blueTextFieldColors()
                )

                OutlinedTextField(
                    value = tempProfile.upiId,
                    onValueChange = { onProfileChange(tempProfile.copy(upiId = it)) },
                    label = { Text("UPI ID") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditing,
                    colors = blueTextFieldColors()
                )
            }

            // Address
            OutlinedTextField(
                value = tempProfile.address,
                onValueChange = { onProfileChange(tempProfile.copy(address = it)) },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing,
                minLines = 3,
                colors = blueTextFieldColors()
            )

            // Location
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tempProfile.pincode,
                    onValueChange = { onProfileChange(tempProfile.copy(pincode = it)) },
                    label = { Text("Pincode") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditing,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = blueTextFieldColors()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GradientTopAppBar(
    title: String,
    navController: NavController,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title, color = Color.White, fontWeight = FontWeight.SemiBold) },
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
            if (isEditing) {
                IconButton(onClick = onSaveClick) {
                    Icon(Icons.Default.Check, "Save", tint = Color.White)
                }
                IconButton(onClick = onCancelClick) {
                    Icon(Icons.Default.Close, "Cancel", tint = Color.White)
                }
            } else {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, "Edit", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFA880D), Color(0xFFE67C00)) // Changed from Blue600/Blue800 to orange shades
            )
        )
//        modifier = Modifier.background(
//            brush = Brush.verticalGradient(
//                colors = listOf(0xFFFA880D, 0xFFE67C00)
//            )
//        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun blueTextFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = Blue500,
    unfocusedBorderColor = Blue300,
    focusedLabelColor = Blue700,
    unfocusedLabelColor = Blue500,
    cursorColor = Blue700,
    focusedTextColor = Blue900,
    unfocusedTextColor = Blue800
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun disabledTextFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    disabledBorderColor = Blue200,
    disabledTextColor = Blue800.copy(alpha = 0.6f),
    disabledLabelColor = Blue800.copy(alpha = 0.6f)
)

// Add to your theme/Colors.kt
val Blue50 = Color(0xFFE3F2FD)
val Blue100 = Color(0xFFBBDEFB)
val Blue200 = Color(0xFF90CAF9)
val Blue300 = Color(0xFF64B5F6)
val Blue400 = Color(0xFF42A5F5)
val Blue500 = Color(0xFF2196F3)
val Blue600 = Color(0xFF1E88E5)
val Blue700 = Color(0xFF1976D2)
val Blue800 = Color(0xFF1565C0)
val Blue900 = Color(0xFF0D47A1)
val ErrorRed = Color(0xFFD32F2F)
val SuccessGreen = Color(0xFF388E3C)

class ProfileAuthViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileAuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileAuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}