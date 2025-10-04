package com.kasnady.tests.fammeproductstest

import com.kasnady.tests.fammeproductstest.products.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledJobs(
    private val productService: ProductService,
) {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    // make the scheduled job run immediately at startup.
    @Scheduled(initialDelay = 0)
    fun runOnStartup() {
        logger.info("Initialize scheduled jobs that run on startup")

        productService.fetchAndStoreData()

        logger.info("Finished running scheduled jobs that run on startup")
    }
}
