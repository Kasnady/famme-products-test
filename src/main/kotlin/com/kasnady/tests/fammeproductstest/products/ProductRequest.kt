package com.kasnady.tests.fammeproductstest.products

data class ProductRequest(
    val title: String,
    val handle: String,
    val productType: String,
    val variants: List<ProductVariantRequest> = emptyList(),
)
