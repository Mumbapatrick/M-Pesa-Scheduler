package com.fintech.mpesascheduler.repository


import com.fintech.mpesascheduler.entity.MpesaTransaction
import org.springframework.data.jpa.repository.JpaRepository

interface MpesaTransactionRepository : JpaRepository<MpesaTransaction, Long>
