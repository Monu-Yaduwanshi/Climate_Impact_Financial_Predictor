//package com.example.climatexpert.DrawerContent
//
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.io.IOException
//import java.util.UUID
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class InsuranceRepository {
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
//        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
//        .build()
//
//    private val mediaType = "application/json".toMediaType()
//
//    suspend fun getPolicyDocument(requestBody: String): String = withContext(Dispatchers.IO) {
//        val request = Request.Builder()
//            .url("https://sandbox.api-setu.in/certificate/v3/iffcotokio/podoc")
//            .post(requestBody.toRequestBody(mediaType))
//            .addHeader("X-APISETU-APIKEY", "demokey123456ABCD789")
//            .addHeader("X-APISETU-CLIENTID", "in.gov.sandbox")
//            .addHeader("Content-Type", "application/json")
//            .addHeader("Accept", "application/json")
//            .build()
//
//        try {
//            val response = client.newCall(request).execute()
//            val responseBody = response.body?.string() ?: ""
//
//            when {
//                response.isSuccessful -> responseBody
//                response.code == 400 -> {
//                    throw MissingParameterException("Missing required parameters: $responseBody")
//                }
//                else -> throw IOException("API Error ${response.code}: $responseBody")
//            }
//        } catch (e: Exception) {
//            throw IOException("Network error: ${e.message}")
//        }
//    }
//}
//
//class MissingParameterException(message: String) : IOException(message)