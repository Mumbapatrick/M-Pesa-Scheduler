package com.fintech.mpesascheduler.controller

import com.fintech.mpesascheduler.dto.StkPushRequest
import com.fintech.mpesascheduler.entity.*
import com.fintech.mpesascheduler.repository.*
import com.fintech.mpesascheduler.service.MpesaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/mpesa")
class MpesaController(
    private val mpesaService: MpesaService,
    private val mpesaTransactionRepository: MpesaTransactionRepository,
    private val batchTransactionRepository: BatchTransactionRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val paymentLogRepository: PaymentLogRepository,
    private val userRepository: UserRepository
) {

    // --------------------------
    // STK PUSH
    // --------------------------
    @PostMapping("/stkpush")
    fun initiateStkPush(@RequestBody request: StkPushRequest): ResponseEntity<String> {
        val response = mpesaService.initiateStkPush(request)
        return ResponseEntity.ok(response)
    }

    // --------------------------
    // MPESA TRANSACTIONS
    // --------------------------
    @GetMapping("/transactions")
    fun getAllTransactions(): List<MpesaTransaction> = mpesaTransactionRepository.findAll()

    @GetMapping("/transactions/{id}")
    fun getTransaction(@PathVariable id: Long): ResponseEntity<MpesaTransaction> {
        val transaction = mpesaTransactionRepository.findById(id)
        return transaction.map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
    }

    @PostMapping("/transactions")
    fun createTransaction(@RequestBody transaction: MpesaTransaction): ResponseEntity<MpesaTransaction> {
        // Validate related user and batchTransaction
        val user = transaction.user?.id?.let { userRepository.findById(it).orElse(null) }
        val batch = transaction.batchTransaction?.id?.let { batchTransactionRepository.findById(it).orElse(null) }

        val saved = mpesaTransactionRepository.save(
            transaction.copy(
                user = user,
                batchTransaction = batch,
                transactionDate = LocalDateTime.now()
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }

    // --------------------------
    // BATCH TRANSACTIONS
    // --------------------------
    @GetMapping("/batches")
    fun getAllBatches(): List<BatchTransaction> = batchTransactionRepository.findAll()

    @GetMapping("/batches/{id}")
    fun getBatch(@PathVariable id: Long): ResponseEntity<BatchTransaction> {
        val batch = batchTransactionRepository.findById(id)
        return batch.map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
    }

    @PostMapping("/batches")
    fun createBatch(@RequestBody batch: BatchTransaction): ResponseEntity<BatchTransaction> {
        val user = batch.user?.id?.let { userRepository.findById(it).orElse(null) }
        val saved = batchTransactionRepository.save(batch.copy(user = user, createdAt = LocalDateTime.now()))
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }

    // --------------------------
    // SCHEDULED PAYMENTS
    // --------------------------
    @GetMapping("/scheduled")
    fun getAllScheduledPayments(): List<ScheduledPayment> = scheduledPaymentRepository.findAll()

    @PostMapping("/scheduled")
    fun schedulePayment(@RequestBody payment: ScheduledPayment): ResponseEntity<ScheduledPayment> {
        val user = payment.user?.id?.let { userRepository.findById(it).orElse(null) }
        val saved = scheduledPaymentRepository.save(payment.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            processed = false,
            user = user
        ))
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }

    // --------------------------
    // PAYMENT LOGS
    // --------------------------
    @GetMapping("/logs")
    fun getAllPaymentLogs(): List<PaymentLog> = paymentLogRepository.findAll()

    @GetMapping("/logs/transaction/{transactionId}")
    fun getLogsByTransaction(@PathVariable transactionId: Long): List<PaymentLog> =
        paymentLogRepository.findByTransactionId(transactionId)

    @PostMapping("/logs")
    fun createPaymentLog(@RequestBody log: PaymentLog): ResponseEntity<PaymentLog> {
        val transaction = log.transaction?.id?.let { mpesaTransactionRepository.findById(it).orElse(null) }
        val saved = paymentLogRepository.save(log.copy(
            transaction = transaction,
            logTime = LocalDateTime.now(),
            createdAt = LocalDateTime.now()
        ))
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
}
