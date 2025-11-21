package com.fintech.mpesascheduler.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "batch_transaction")
data class BatchTransaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val batchName: String,

    @Column(name = "total_amount", nullable = false)
    val totalAmount: Double,

    @Column(name = "split_type", nullable = false)
    val splitType: String = "EQUAL",   // EQUAL or CUSTOM

    @Column(name = "transaction_count", nullable = false)
    val transactionCount: Int,

    @Column(nullable = false)
    var status: String = "PENDING",   // PENDING, COMPLETED, FAILED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserAccount? = null,

    @JsonIgnore
    @OneToMany(
        mappedBy = "batchTransaction",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val transactions: List<MpesaTransaction> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
