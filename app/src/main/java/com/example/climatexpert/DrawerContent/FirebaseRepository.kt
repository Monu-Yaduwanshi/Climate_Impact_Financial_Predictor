package com.example.climatexpert.DrawerContent

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Cart operations
    fun getUserCartRef(): DatabaseReference {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        return database.child("user_carts").child(userId)
    }

    // Order operations
    fun getOrdersRef(): DatabaseReference {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        return database.child("user_orders").child(userId)
    }

    // Profile operations
    fun getUserProfileRef(): DatabaseReference {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        return database.child("user_profiles").child(userId)
    }
}