package com.kasnady.tests.fammeproductstest.utils.network

import com.kasnady.tests.fammeproductstest.products.models.Product
import com.kasnady.tests.fammeproductstest.utils.network.response.ProductResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


interface ApiService {
    fun fetchProducts(): List<Product>?
}

@Service
class ApiServiceImpl(
    private val webClientFamme: WebClient
): ApiService {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun fetchProducts(): List<Product> {
        return webClientFamme.get()
            .uri("products.json")
            .retrieve()
                .bodyToMono(ProductResponse::class.java)
            .block() // block() makes it synchronous, remove if you want reactive
            ?.products ?: emptyList()
    }
}
