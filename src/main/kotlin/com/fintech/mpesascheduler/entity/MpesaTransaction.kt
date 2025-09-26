package com.fintech.mpesascheduler.entity

import jakarta.persistence.*

@Entity
data class MpesaTransaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val phoneNumber: String,
    val amount: Double,
    val reference: String,
    val checkoutRequestId: String
)
