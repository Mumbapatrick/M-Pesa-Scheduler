package com.fintech.mpesascheduler.dto

data class StkPushRequest(
    val phoneNumber: String,
    val amount: Double,
    val accountReference: String,
    val transactionDesc: String
)