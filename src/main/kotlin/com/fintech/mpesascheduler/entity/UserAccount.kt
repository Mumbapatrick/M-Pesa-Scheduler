package com.fintech.mpesascheduler.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_account",
    indexes = [
        Index(name = "idx_user_email", columnList = "email"),
        Index(name = "idx_user_phone", columnList = "phone_number")
    ]
)
data class UserAccount(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(name = "phone_number", unique = true)
    val phoneNumber: String? = null,

    @Column(name = "password_hash", nullable = false, length = 255)
    val passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole = UserRole.USER,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    // -----------------------------
    // RELATIONSHIPS
    // -----------------------------

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val transactions: List<MpesaTransaction> = emptyList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val batchTransactions: List<BatchTransaction> = emptyList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val scheduledPayments: List<ScheduledPayment> = emptyList()
)
