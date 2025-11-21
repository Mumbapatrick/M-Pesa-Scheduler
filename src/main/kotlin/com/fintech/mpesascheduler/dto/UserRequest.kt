package com.fintech.mpesascheduler.dto

data class UserRequest(
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val password: String
)
