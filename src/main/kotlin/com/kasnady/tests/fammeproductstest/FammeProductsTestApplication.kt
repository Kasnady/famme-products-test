package com.kasnady.tests.fammeproductstest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FammeProductsTestApplication

fun main(args: Array<String>) {
    runApplication<FammeProductsTestApplication>(*args)
}
