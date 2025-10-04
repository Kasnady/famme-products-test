package com.kasnady.tests.fammeproductstest.products.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class Product(
    val id: Long,
    val title: String,
    val handle: String,
    @JsonProperty("product_type")
    val productType: String,
    @JsonProperty("created_at")
    val creationTimestamp: Timestamp,
    @JsonProperty("updated_at")
    val lastUpdateTimestamp: Timestamp,
    val variants: List<ProductVariant>? = null,
)
