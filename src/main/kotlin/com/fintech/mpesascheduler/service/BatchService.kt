package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.entity.BatchTransaction
import com.fintech.mpesascheduler.repository.BatchTransactionRepository
import org.springframework.stereotype.Service

@Service
class BatchTransactionService(
    private val repository: BatchTransactionRepository
) {
    fun createBatch(batch: BatchTransaction): BatchTransaction =
        repository.save(batch)

    fun getAllBatches(): List<BatchTransaction> =
        repository.findAll()

    fun getBatchById(id: Long): BatchTransaction? =
        repository.findById(id).orElse(null)

    fun updateStatus(id: Long, status: String): BatchTransaction? {
        val batch = repository.findById(id).orElse(null) ?: return null
        val updated = batch.copy(status = status)
        return repository.save(updated)
    }
}
