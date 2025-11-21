package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.dto.StkPushRequest
import com.fintech.mpesascheduler.enums.PaymentStatus
import com.fintech.mpesascheduler.repository.ScheduledPaymentRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduledPaymentService(
    private val repository: ScheduledPaymentRepository,
    private val mpesaService: MpesaService,
    private val logService: PaymentLogService
) {
    private val logger = LoggerFactory.getLogger(ScheduledPaymentService::class.java)

    @Scheduled(fixedRate = 60000) // runs every 1 minute
    fun processDuePayments() {
        val duePayments = repository.findByScheduleTimeBeforeAndStatus(LocalDateTime.now())
        logger.info("Found ${duePayments.size} due payments to process")

        duePayments.forEach { payment ->
            try {
                // Mark as processing
                payment.status = PaymentStatus.PROCESSING
                payment.updatedAt = LocalDateTime.now()
                repository.save(payment)

                // Initiate STK push
                val response = mpesaService.initiateStkPush(payment.toStkPushRequest())
                logger.info("Processed payment ID ${payment.id} → $response")

                // Mark as successful
                payment.status = PaymentStatus.SUCCESSFUL
                payment.updatedAt = LocalDateTime.now()
                repository.save(payment)

                // Log success
                logService.logAction(
                    action = "STK_PUSH",
                    status = "SUCCESSFUL",
                    accountReference = payment.reference ?: "Payment-${payment.id}",
                    transactionDesc = "Scheduled payment processing",
                    details = "Payment ID ${payment.id} processed successfully"
                )

            } catch (ex: Exception) {
                // Mark as failed
                payment.status = PaymentStatus.FAILED
                payment.updatedAt = LocalDateTime.now()
                repository.save(payment)

                // Log failure
                logService.logAction(
                    action = "STK_PUSH",
                    status = "FAILED",
                    accountReference = payment.reference ?: "Payment-${payment.id}",
                    transactionDesc = "Scheduled payment processing",
                    details = "Payment ID ${payment.id} failed: ${ex.message}"
                )

                logger.error("Failed to process payment ID ${payment.id}: ${ex.message}", ex)
            }
        }
    }

    // Extension function to convert ScheduledPayment to StkPushRequest
    private fun com.fintech.mpesascheduler.entity.ScheduledPayment.toStkPushRequest(): StkPushRequest {
        return StkPushRequest(
            phoneNumber = this.phoneNumber,
            amount = this.amount,
            accountReference = this.reference ?: "Payment-${this.id}", // Added
            transactionDesc = "Scheduled payment processing"            // Added
        )
    }
}
