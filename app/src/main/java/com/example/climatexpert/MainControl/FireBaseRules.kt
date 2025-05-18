//package com.example.climatexpert.MainControl
//
//// ClimateXpert Firebase Rules
//{
//    "rules": {
//    ".read": "auth != null",
//    ".write": "auth != null",
//
//    "user_profiles": {
//    "$uid": {
//    ".read": "auth != null && auth.uid == $uid",
//    ".write": "auth != null && auth.uid == $uid"
//}
//},
//
//    "user_carts": {
//    "$uid": {
//    ".read": "auth != null && auth.uid == $uid",
//    ".write": "auth != null && auth.uid == $uid"
//}
//},
//
//    "user_orders": {
//    "$uid": {
//    "$orderId": {
//    ".read": "auth != null && auth.uid == $uid",
//    ".write": "auth != null && auth.uid == $uid"
//}
//}
//},
//
//    // // Optional: Add indexes for better querying
//    // "user_orders": {
//    //   ".indexOn": ["userId", "orderDate"]
//    // },
//
//    // "user_carts": {
//    //   ".indexOn": ["userId"]
//    // }
//}
//}