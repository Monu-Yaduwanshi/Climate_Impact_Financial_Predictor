package com.example.climatexpert.MainControl

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserInformation")

    private val _loginState = MutableLiveData<AuthState>(AuthState.Idle)
    val loginState: LiveData<AuthState> get() = _loginState

    private val _signUpState = MutableLiveData<AuthState>(AuthState.Idle)
    val signUpState: LiveData<AuthState> get() = _signUpState

    // 🔹 Authentication State
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val userId: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    // 🔹 User Data Model
    data class UserInformation(
        val uid: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = ""
    )

    // ✅ LOGIN FUNCTION (Now fetches user data from Realtime DB)
    fun login(email: String, password: String, context: Context, navController: NavController) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "All fields must be filled!", Toast.LENGTH_SHORT).show()
            return
        }

        _loginState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: ""

                    // ✅ Fetch user details from Realtime Database
                    db.child(userId).get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            _loginState.value = AuthState.Success(userId)
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                            // ✅ Navigate to "home" after login
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            _loginState.value = AuthState.Error("User not found in database!")
                            Toast.makeText(context, "User not found in database!", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { e ->
                        _loginState.value = AuthState.Error("Database fetch error: ${e.message}")
                        Toast.makeText(context, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "No account found with this email."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid password. Try again."
                        else -> task.exception?.localizedMessage ?: "Login failed. Try again."
                    }
                    _loginState.value = AuthState.Error(errorMessage)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // ✅ SIGN-UP FUNCTION (Ensures data is stored in Realtime Database)
    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        context: Context,
        navController: NavController
    ) {
        if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            Toast.makeText(context, "All fields must be filled!", Toast.LENGTH_SHORT).show()
            return
        }

        _signUpState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: ""

                    val user = UserInformation(
                        uid = userId,
                        firstName = firstName,
                        lastName = lastName,
                        email = email
                    )

                    // ✅ Save user data in Realtime Database
                    db.child(userId).setValue(user)
                        .addOnSuccessListener {
                            _signUpState.value = AuthState.Success(userId)
                            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                            // ✅ Navigate to "home" after signup
                            navController.navigate("home") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseError", "Database write failed: ${e.message}")
                            _signUpState.value =
                                AuthState.Error("Failed to save user data: ${e.message}")
                            Toast.makeText(context, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters."
                        is FirebaseAuthUserCollisionException -> "Email already in use. Try another one."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                        else -> task.exception?.localizedMessage ?: "Sign-up failed. Try again."
                    }
                    _signUpState.value = AuthState.Error(errorMessage)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun resetLoginState() {
        _loginState.value = AuthState.Idle
    }

    fun resetSignUpState() {
        _signUpState.value = AuthState.Idle
    }
}
