package com.kasnady.tests.fammeproductstest.products

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/")
    fun showProducts(): String {
        return "products"
    }

    @GetMapping("/products")
    fun productsTable(model: Model): String {
        val products = productService.getProducts()

        model.addAttribute("products", products)
        return "fragments/productTable :: table" // returns only the table fragment
    }
}
