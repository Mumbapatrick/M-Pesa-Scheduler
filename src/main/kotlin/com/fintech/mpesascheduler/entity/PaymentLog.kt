package com.fintech.mpesascheduler.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "payment_log")
data class PaymentLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    // Existing fields for logPayment
    @Column(name = "request_payload", columnDefinition = "jsonb", nullable = true)
    val requestPayload: String? = null,

    @Column(name = "response_payload", columnDefinition = "jsonb", nullable = true)
    val responsePayload: String? = null,

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    val transaction: MpesaTransaction? = null,

    @Column(name = "log_time")
    val logTime: LocalDateTime = LocalDateTime.now(),

    // New fields for logAction
    @Column(name = "action")
    val action: String? = null,

    @Column(name = "status")
    val status: String? = null,

    @Column(name = "account_reference")
    val accountReference: String? = null,

    @Column(name = "transaction_desc")
    val transactionDesc: String? = null,

    @Column(name = "details")
    val details: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
