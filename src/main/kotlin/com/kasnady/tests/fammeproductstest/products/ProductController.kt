package com.kasnady.tests.fammeproductstest.products

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/")
    fun showProducts(model: Model): String {
        model.addAttribute("errors", emptyMap<String, String>())
        model.addAttribute("showSuccess", false)

        return "products"
    }

    @GetMapping("/products")
    fun productsTable(model: Model): String {
        val products = productService.getProducts()

        model.addAttribute("products", products)
        return "fragments/productTable :: table" // returns only the table fragment
    }

    @PostMapping("/products")
    fun saveProduct(
        @ModelAttribute product: ProductRequest,
        bindingResult: BindingResult,
        model: Model,
        response: HttpServletResponse,
    ): String {
        val errors = mutableMapOf<String, String>()

        if (bindingResult.hasErrors()) {
            errors["api"] = bindingResult.allErrors.joinToString(", ") { it.defaultMessage ?: "Invalid field" }
        } else if (product.variants.isEmpty()) {
            errors["api"] = "Need to have at least 1 product variant"
        } else {
            // TODO: Might need to add validation if needed here

            productService.saveProductAndVariants(product)

            // ✅ Trigger table refresh only on success
            response.setHeader("HX-Trigger", "tableRefresh")
        }

        model.addAttribute("errors", errors)
        model.addAttribute("showSuccess", errors.isEmpty())
        return "fragments/form :: formFragment"
    }
}
