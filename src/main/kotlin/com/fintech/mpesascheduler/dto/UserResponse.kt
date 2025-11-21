package com.fintech.mpesascheduler.dto

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val role: String,
    val isActive: Boolean
)
