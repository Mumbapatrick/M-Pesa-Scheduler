package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.config.MpesaProperties
import com.fintech.mpesascheduler.dto.StkPushRequest
import com.fintech.mpesascheduler.entity.MpesaTransaction
import com.fintech.mpesascheduler.repository.MpesaTransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class MpesaService(
    private val restTemplate: RestTemplate,
    private val props: MpesaProperties,
    private val repository: MpesaTransactionRepository
) {

    private val logger = LoggerFactory.getLogger(MpesaService::class.java)

    /**
     * ======================
     *  STK PUSH MAIN LOGIC
     * ======================
     */
    fun initiateStkPush(request: StkPushRequest): String {
        logger.info("🔄 Initiating STK Push | Phone=${request.phoneNumber}, Amount=${request.amount}")

        val token = getAccessToken()
        val timestamp = generateTimestamp()
        val password = generatePassword(timestamp)

        val transaction = repository.save(
            MpesaTransaction(
                targetValue = request.phoneNumber,
                transactionType = "INDIVIDUAL",
                amount = request.amount,
                reference = request.accountReference,
                phoneNumber = request.phoneNumber,
                status = "PENDING",
                transactionDate = LocalDateTime.now()
            )
        )

        /**
         *  YOUR CALLBACK URL SHOULD BE FROM YOUR SERVER, NOT MPESA BASE URL
         */

        val callbackUrl = "http://localhost:8080/api/mpesa/callback/${transaction.id}"

        val stkBody = mapOf(
            "BusinessShortCode" to props.shortCode,
            "Password" to password,
            "Timestamp" to timestamp,
            "TransactionType" to "CustomerPayBillOnline",
            "Amount" to request.amount.toInt(),
            "PartyA" to request.phoneNumber,
            "PartyB" to props.shortCode,
            "PhoneNumber" to request.phoneNumber,
            "CallBackURL" to callbackUrl,
            "AccountReference" to request.accountReference,
            "TransactionDesc" to request.transactionDesc
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $token")
        }

        return try {
            val url = "${props.baseUrl}/mpesa/stkpush/v1/processrequest"
            logger.info("📡 Sending STK Push → $url")

            val response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntity(stkBody, headers),
                object : ParameterizedTypeReference<Map<String, Any>>() {}
            )

            val body = response.body ?: emptyMap()

            transaction.apply {
                checkoutRequestId = body["CheckoutRequestID"]?.toString()
                responseCode = body["ResponseCode"]?.toString()
                responseDescription = body["ResponseDescription"]?.toString()
                status = if (responseCode == "0") "REQUEST_SENT" else "FAILED"
            }

            repository.save(transaction)

            logger.info("✅ STK Push request completed for TxID: ${transaction.id}")
            "STK Push initiated successfully"

        } catch (ex: Exception) {
            logger.error("❌ STK Push Error: ${ex.message}")

            transaction.status = "FAILED"
            transaction.responseDescription = ex.message
            repository.save(transaction)

            "STK Push failed → ${ex.message}"
        }
    }

    /**
     * ======================
     *  FETCH ACCESS TOKEN
     * ======================
     */
    private fun getAccessToken(): String {
        val url = "${props.baseUrl}/oauth/v1/generate?grant_type=client_credentials"

        return try {
            val headers = HttpHeaders().apply {
                setBasicAuth(props.consumerKey, props.consumerSecret)
            }

            val response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity<String>(headers),
                object : ParameterizedTypeReference<Map<String, Any>>() {}
            )

            val token = response.body?.get("access_token")?.toString()
                ?: throw RuntimeException("No access_token returned")

            logger.info("🔑 Daraja Access Token Fetched")
            token

        } catch (ex: Exception) {
            logger.error("❌ Failed to fetch token: ${ex.message}")
            throw ex
        }
    }

    private fun generatePassword(timestamp: String): String {
        val raw = props.shortCode + props.passKey + timestamp
        return Base64.getEncoder().encodeToString(raw.toByteArray())
    }

    private fun generateTimestamp(): String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
}
