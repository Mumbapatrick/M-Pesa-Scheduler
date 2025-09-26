package com.fintech.mpesascheduler.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mpesa")
class MpesaProperties {
    lateinit var consumerKey: String
    lateinit var consumerSecret: String
    lateinit var shortCode: String
    lateinit var passKey: String
    lateinit var baseUrl: String
}
