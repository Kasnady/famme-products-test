package com.kasnady.tests.fammeproductstest.products

import com.kasnady.tests.fammeproductstest.products.models.Product
import com.kasnady.tests.fammeproductstest.utils.network.ApiService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProductService {
    fun fetchAndStoreData()

    fun getProducts(): List<Product>
}

@Service
@Transactional
class ProductServiceImpl(
    private val apiService: ApiService,
    private val productRepository: ProductRepository,
): ProductService {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun fetchAndStoreData() {
        logger.info("Start fetching and store products to DB if necessary")

        val existingProducts = productRepository.getProducts()
        if (existingProducts.isNotEmpty() && existingProducts.size >= 30) {
            logger.info(
                "Abort fetching products data due to existing products size already at 30 or more. Current size: ${existingProducts.size}"
            )
            return
        }

        // Only need to run once
        val products = apiService.fetchProducts()
        if (products != null && products.isNotEmpty()) {
            logger.info("Saving products into DB with size: ${products.size}")
            productRepository.saveProducts(products)

            logger.info("Saved products into DB and continuing to Variants")

            val allVariants = products.flatMap { it.variants ?: emptyList() }
            if (allVariants.isNotEmpty()) {
                logger.info("Saving product variants into DB with size: ${allVariants.size}")
                productRepository.saveVariants(allVariants)
            }
        }

        logger.info("Finish fetching and store products to DB")
    }

    override fun getProducts(): List<Product> {
        return productRepository.getProducts()
    }
}
