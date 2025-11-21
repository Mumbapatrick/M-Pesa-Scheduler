package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.entity.MpesaTransaction
import com.fintech.mpesascheduler.repository.MpesaTransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val repository: MpesaTransactionRepository
) {
    fun findAll(): List<MpesaTransaction> = repository.findAll()

    fun findById(id: Long): MpesaTransaction? =
        repository.findById(id).orElse(null)

    fun updateStatus(id: Long, status: String): MpesaTransaction? {
        val tx = repository.findById(id).orElse(null) ?: return null
        val updated = tx.copy(status = status)
        return repository.save(updated)
    }
}
