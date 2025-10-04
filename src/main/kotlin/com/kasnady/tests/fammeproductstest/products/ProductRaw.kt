package com.kasnady.tests.fammeproductstest.products

import java.sql.Timestamp

data class ProductRaw(
    val productId: Long,
    val productTitle: String,
    val productHandle: String,
    val productType: String,
    val creationTimestamp: Timestamp,
    val lastUpdateTimestamp: Timestamp,
    val variantId: Long,
    val variantTitle: String,
    val sku: String,
    val price: String,
)
