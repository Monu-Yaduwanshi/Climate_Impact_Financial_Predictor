//package com.example.climatexpert.DrawerContent
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import org.json.JSONObject
//import java.io.IOException
//import java.util.*
//
//class InsuranceViewModel(
//    private val repository: InsuranceRepository
//) : ViewModel() {
//    private val _insuranceResponse = MutableStateFlow<InsuranceResponse?>(null)
//    val insuranceResponse: StateFlow<InsuranceResponse?> = _insuranceResponse.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//
//    fun fetchPolicyDocument() {
//        _isLoading.value = true
//        _error.value = null
//
//        viewModelScope.launch {
//            try {
//                val requestBody = createCompleteRequestBody()
//                val response = repository.getPolicyDocument(requestBody)
//                _insuranceResponse.value = parseResponse(response)
//            } catch (e: MissingParameterException) {
//                _error.value = "Missing parameters: ${e.message}"
//            } catch (e: IOException) {
//                _error.value = "Network error: ${e.message}"
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message ?: "Unknown error"}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    private fun createCompleteRequestBody(): String {
//        return JSONObject().apply {
//            put("txnId", UUID.randomUUID().toString())
//            put("format", "xml")
//
//            // Certificate Parameters
//            put("certificateParameters", JSONObject().apply {
//                put("POLNO", "MF905732H0018018") // Example policy number
//                put("INCPDT", "24/10/2023") // Example date
//            })
//
//            // Complete Consent Artifact
//            put("consentArtifact", JSONObject().apply {
//                put("consent", JSONObject().apply {
//                    put("consentId", UUID.randomUUID().toString())
//                    put("timestamp", "2024-01-01T00:00:00Z")
//
//                    // Data Consumer
//                    put("dataConsumer", JSONObject().apply {
//                        put("id", "consumer-id-123")
//                    })
//
//                    // Data Provider
//                    put("dataProvider", JSONObject().apply {
//                        put("id", "provider-id-456")
//                    })
//
//                    // Purpose
//                    put("purpose", JSONObject().apply {
//                        put("description", "Policy verification")
//                    })
//
//                    // User
//                    put("user", JSONObject().apply {
//                        put("idType", "MOBILE")
//                        put("idNumber", "9876543210")
//                        put("mobile", "9876543210")
//                        put("email", "user@example.com")
//                    })
//
//                    // Data
//                    put("data", JSONObject().apply {
//                        put("id", "policy-data-789")
//                    })
//
//                    // Permission
//                    put("permission", JSONObject().apply {
//                        put("access", "read")
//
//                        // Date Range
//                        put("dateRange", JSONObject().apply {
//                            put("from", "2024-01-01T00:00:00Z")
//                            put("to", "2025-01-01T00:00:00Z")
//                        })
//
//                        // Frequency
//                        put("frequency", JSONObject().apply {
//                            put("unit", "year")
//                            put("value", 1)
//                            put("repeats", 0)
//                        })
//                    })
//                })
//
//                // Signature
//                put("signature", JSONObject().apply {
//                    put("signature", "sample-signature-xyz")
//                })
//            })
//        }.toString()
//    }
//
//    private fun parseResponse(response: String): InsuranceResponse {
//        val json = JSONObject(response)
//        return InsuranceResponse(
//            txnId = json.getString("txnId"),
//            format = json.getString("format"),
//            certificateParameters = CertificateParameters(
//                POLNO = json.getJSONObject("certificateParameters").getString("POLNO"),
//                INCPDT = json.getJSONObject("certificateParameters").getString("INCPDT")
//            ),
//            consentArtifact = json.getJSONObject("consentArtifact").let { artifact ->
//                ConsentArtifact(
//                    consent = Consent(
//                        consentId = artifact.getJSONObject("consent").getString("consentId"),
//                        timestamp = artifact.getJSONObject("consent").getString("timestamp"),
//                        dataConsumer = DataConsumer(
//                            artifact.getJSONObject("consent")
//                                .getJSONObject("dataConsumer").getString("id")
//                        ),
//                        dataProvider = DataProvider(
//                            artifact.getJSONObject("consent")
//                                .getJSONObject("dataProvider").getString("id")
//                        ),
//                        purpose = Purpose(
//                            artifact.getJSONObject("consent")
//                                .getJSONObject("purpose").getString("description")
//                        ),
//                        user = User(
//                            idType = artifact.getJSONObject("consent")
//                                .getJSONObject("user").getString("idType"),
//                            idNumber = artifact.getJSONObject("consent")
//                                .getJSONObject("user").getString("idNumber"),
//                            mobile = artifact.getJSONObject("consent")
//                                .getJSONObject("user").getString("mobile"),
//                            email = artifact.getJSONObject("consent")
//                                .getJSONObject("user").getString("email")
//                        ),
//                        data = Data(
//                            artifact.getJSONObject("consent")
//                                .getJSONObject("data").getString("id")
//                        ),
//                        permission = Permission(
//                            access = artifact.getJSONObject("consent")
//                                .getJSONObject("permission").getString("access"),
//                            dateRange = DateRange(
//                                from = artifact.getJSONObject("consent")
//                                    .getJSONObject("permission")
//                                    .getJSONObject("dateRange").getString("from"),
//                                to = artifact.getJSONObject("consent")
//                                    .getJSONObject("permission")
//                                    .getJSONObject("dateRange").getString("to")
//                            ),
//                            frequency = Frequency(
//                                unit = artifact.getJSONObject("consent")
//                                    .getJSONObject("permission")
//                                    .getJSONObject("frequency").getString("unit"),
//                                value = artifact.getJSONObject("consent")
//                                    .getJSONObject("permission")
//                                    .getJSONObject("frequency").getInt("value"),
//                                repeats = artifact.getJSONObject("consent")
//                                    .getJSONObject("permission")
//                                    .getJSONObject("frequency").getInt("repeats")
//                            )
//                        )
//                    ),
//                    signature = Signature(
//                        artifact.getJSONObject("signature").getString("signature")
//                    )
//                )
//            }
//        )
//    }
//}