package com.kasnady.tests.fammeproductstest.products.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class ProductVariant(
    val id: Long,
    @JsonProperty("product_id")
    val productId: Long,
    val title: String,
    val sku: String,
    val price: String,
    @JsonProperty("created_at")
    val creationTimestamp: Timestamp,
    @JsonProperty("updated_at")
    val lastUpdateTimestamp: Timestamp,
)
