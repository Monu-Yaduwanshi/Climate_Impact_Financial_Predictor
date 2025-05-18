package com.example.climatexpert.DrawerContent

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class ProfileAuthViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData Declarations
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    private val _profileData = MutableLiveData<UserProfile?>()
    val profileData: LiveData<UserProfile?> get() = _profileData

    // Firebase and Cloudinary Config
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("user_profiles")
    private val cloudinaryCloudName = "dfkfuassi"
    private val cloudinaryUploadPreset = "thread"
    @IgnoreExtraProperties
    data class UserProfile(
        val uid: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val address: String = "",
        val upiId: String = "",
        val mobileNumber: String = "",
        val pincode: String = "",
        val profileImageUrl: String = ""
    ) {
        constructor() : this("", "", "", "", "", "", "", "", "")
    }
//    data class UserProfile(
//        val uid: String = "",
//        val firstName: String = "",
//        val lastName: String = "",
//        val email: String = "",
//        val address: String = "",
//        val upiId: String = "",
//        val mobileNumber: String = "",
//        val pincode: String = "",
//        val profileImageUrl: String = ""
//    )

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        auth.addAuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let { user ->
                fetchUserProfile(user.uid)
            } ?: run {
                _profileData.value = null
            }
        }
    }

    fun fetchUserProfile(uid: String) {
        _isLoading.value = true
        database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _profileData.value = snapshot.getValue(UserProfile::class.java) ?: UserProfile(
                    uid = uid,
                    email = auth.currentUser?.email ?: ""
                )
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _uploadStatus.value = "Failed to load profile: ${error.message}"
                _isLoading.value = false
            }
        })
    }

    fun saveUserProfile(profile: UserProfile) {
        _isLoading.value = true
        database.child(profile.uid).setValue(profile)
            .addOnSuccessListener {
                _profileData.value = profile
                _uploadStatus.value = "Profile saved successfully"
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _uploadStatus.value = "Failed to save profile: ${e.message}"
                _isLoading.value = false
            }
    }

    fun uploadImageToCloudinary(imageUri: Uri) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val context = getApplication<Application>().applicationContext
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val file = File.createTempFile("upload_${System.currentTimeMillis()}", ".jpg").apply {
                    FileOutputStream(this).use { output ->
                        inputStream?.copyTo(output)
                    }
                }

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.name,
                        RequestBody.create("image/*".toMediaTypeOrNull(), file))
                    .addFormDataPart("upload_preset", cloudinaryUploadPreset)
                    .addFormDataPart("cloud_name", cloudinaryCloudName)
                    .addFormDataPart("folder", "climatexpert_profiles")
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

                val request = Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/$cloudinaryCloudName/image/upload")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val json = JSONObject(responseBody)
                    val imageUrl = json.getString("secure_url")

                    withContext(Dispatchers.Main) {
                        val currentProfile = _profileData.value ?: UserProfile()
                        saveUserProfile(currentProfile.copy(profileImageUrl = imageUrl))
                    }
                } else {
                    _uploadStatus.postValue("Upload failed: ${response.code} - $responseBody")
                }
            } catch (e: Exception) {
                _uploadStatus.postValue("Upload error: ${e.localizedMessage}")
                Log.e("CloudinaryUpload", "Error:", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}