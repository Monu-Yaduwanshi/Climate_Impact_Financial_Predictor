package com.example.climatexpert.DrawerContent


import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserCart(
    val userId: String = "",
    val items: List<InsuranceItem> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
) {
    constructor() : this("", emptyList(), 0L)
}

// Data/Order.kt
@IgnoreExtraProperties
data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<InsuranceItem> = emptyList(),
    val totalAmount: Int = 0,
    val paymentMethod: String = "",
    val orderDate: Long = System.currentTimeMillis(),
    val status: String = "Pending"
) {
    constructor() : this("", "", emptyList(), 0, "", 0L, "Pending")
}