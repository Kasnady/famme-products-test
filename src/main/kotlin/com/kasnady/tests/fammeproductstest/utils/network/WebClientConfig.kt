package com.kasnady.tests.fammeproductstest.utils.network

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    @Value("\${network.famme.host}")
    private val fammeHostUrl: String,
) {
    @Bean
    fun webClientFamme(builder: WebClient.Builder): WebClient {
        val strategies = ExchangeStrategies.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024) // 1 MB
            }
            .build()

        return builder
            .baseUrl(fammeHostUrl)
            .exchangeStrategies(strategies)
            .build()
    }
}
