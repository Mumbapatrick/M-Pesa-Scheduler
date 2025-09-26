package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.config.MpesaProperties
import com.fintech.mpesascheduler.dto.StkPushRequest
import com.fintech.mpesascheduler.repository.MpesaTransactionRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class MpesaService(
    private val restTemplate: RestTemplate,
    private val properties: MpesaProperties,
    private val repository: MpesaTransactionRepository
) {

    fun initiateStkPush(request: StkPushRequest): String {
        val token = getAccessToken()

        val timestamp = generateTimestamp()
        val password = generatePassword(timestamp)

        val stkRequestBody = mapOf(
            "BusinessShortCode" to properties.shortCode,
            "Password" to password,
            "Timestamp" to timestamp,
            "TransactionType" to "CustomerPayBillOnline",
            "Amount" to request.amount.toInt(),
            "PartyA" to request.phoneNumber,
            "PartyB" to properties.shortCode,
            "PhoneNumber" to request.phoneNumber,
            "CallBackURL" to "${properties.baseUrl}/api/mpesa/callback",
            "AccountReference" to request.accountReference,
            "TransactionDesc" to request.transactionDesc
        )

        val headers = mapOf("Authorization" to "Bearer $token")

        // TODO: Use restTemplate to POST to Daraja endpoint
        return "STK Push simulated (mocked)"
    }

    private fun getAccessToken(): String {
        // TODO: Use restTemplate and Basic Auth to get access token
        return "dummy-access-token"
    }

    private fun generateTimestamp(): String =
        java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

    private fun generatePassword(timestamp: String): String {
        val dataToEncode = properties.shortCode + properties.passKey + timestamp
        return Base64.getEncoder().encodeToString(dataToEncode.toByteArray())
    }
}
