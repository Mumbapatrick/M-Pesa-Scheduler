package com.fintech.mpesascheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MpesaSchedulerApplication

fun main(args: Array<String>) {
    runApplication<MpesaSchedulerApplication>(*args)
}
