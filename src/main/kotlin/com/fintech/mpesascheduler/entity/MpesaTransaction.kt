package com.fintech.mpesascheduler.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "mpesa_transaction")
data class MpesaTransaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "target_value", nullable = false)
    val targetValue: String,

    @Column(name = "transaction_type", nullable = false)
    val transactionType: String = "INDIVIDUAL",

    @Column(nullable = false)
    val amount: Double,

    val reference: String? = null,

    @Column(name = "checkout_request_id")
    var checkoutRequestId: String? = null,

    @Column(nullable = false)
    var status: String = "PENDING",

    @Column(name = "response_code")
    var responseCode: String? = null,

    @Column(name = "response_description")
    var responseDescription: String? = null,

    // Single FK to batch_transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_transaction_id")
    val batchTransaction: BatchTransaction? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserAccount? = null,

    @Column(name = "phone_number")
    val phoneNumber: String? = null,

    @Column(name = "scan_type")
    val scanType: String? = null,

    @Column(name = "transaction_date")
    val transactionDate: LocalDateTime = LocalDateTime.now()
)
