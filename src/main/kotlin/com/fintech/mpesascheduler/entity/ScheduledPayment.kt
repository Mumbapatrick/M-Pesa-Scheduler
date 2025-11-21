package com.fintech.mpesascheduler.entity

import com.fintech.mpesascheduler.enums.PaymentStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "scheduled_payment")
data class ScheduledPayment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String = "",

    @Column(nullable = false)
    var amount: Double = 0.0,

    @Column
    var reference: String? = null,

    @Column(name = "schedule_time", nullable = false)
    var scheduleTime: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PaymentStatus = PaymentStatus.PENDING,

    @Column(name = "processed", nullable = false)
    var processed: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    // --------------------------------------
    // RELATIONSHIP: ScheduledPayment → User
    // --------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // FK Column
    var user: UserAccount? = null
)
