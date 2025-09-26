package com.fintech.mpesascheduler.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class MpesaConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}
