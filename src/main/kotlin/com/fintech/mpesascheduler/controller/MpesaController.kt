package com.fintech.mpesascheduler.controller


import com.fintech.mpesascheduler.dto.StkPushRequest
import com.fintech.mpesascheduler.entity.MpesaTransaction
import com.fintech.mpesascheduler.repository.MpesaTransactionRepository
import com.fintech.mpesascheduler.service.MpesaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mpesa")
class MpesaController(private val mpesaService: MpesaService,
                      private val mpesaTransactionRepository: MpesaTransactionRepository
) {

    @PostMapping("/stkpush")
    fun initiateStkPush(@RequestBody request: StkPushRequest): ResponseEntity<String> {
        val response = mpesaService.initiateStkPush(request)
        return ResponseEntity.ok(response)
    }
        @GetMapping("/transactions")
        fun getAllTransactions(): List<MpesaTransaction> {
            return mpesaTransactionRepository.findAll()
        }
    }
