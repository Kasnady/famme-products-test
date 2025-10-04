package com.kasnady.tests.fammeproductstest.products

import com.kasnady.tests.fammeproductstest.products.models.Product
import com.kasnady.tests.fammeproductstest.products.models.ProductVariant
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

interface ProductRepository {
    fun getProducts(): List<Product>

    fun saveProducts(products: List<Product>)

    fun saveVariants(variants: List<ProductVariant>)
}

@Repository
@Transactional
class ProductRepositoryImpl(
    private val namedJdbcTemplate: NamedParameterJdbcTemplate,
): ProductRepository {

    override fun getProducts(): List<Product> {
        val contentQuery = """
            SELECT * FROM product
            LIMIT 100
        """.trimIndent()

        return namedJdbcTemplate.query(contentQuery, ProductRowMapper())
    }

    override fun saveProducts(products: List<Product>) {
        if (products.isEmpty()) return

        val sql = """
            INSERT INTO product (
                id, title, handle, product_type,
                creation_timestamp, last_update_timestamp
            ) VALUES (
                :id, :title, :handle, :productType,
                :creationTimestamp, :lastUpdateTimestamp
            )
        """.trimIndent()

        val params = products.map {
            MapSqlParameterSource()
                .addValue("id", it.id)
                .addValue("title", it.title)
                .addValue("handle", it.handle)
                .addValue("productType", it.productType)
                .addValue("creationTimestamp", it.creationTimestamp)
                .addValue("lastUpdateTimestamp", it.lastUpdateTimestamp)
        }.toTypedArray()

        namedJdbcTemplate.batchUpdate(sql, params)
    }

    override fun saveVariants(variants: List<ProductVariant>) {
        if (variants.isEmpty()) return

        val sql = """
            INSERT INTO product_variant (
                id, product_id, title, sku, price,
                creation_timestamp, last_update_timestamp
            ) VALUES (
                :id, :productId, :title, :sku, :price,
                :creationTimestamp, :lastUpdateTimestamp
            )
        """.trimIndent()

        val params = variants.map {
            MapSqlParameterSource()
                .addValue("id", it.id)
                .addValue("productId", it.productId)
                .addValue("title", it.title)
                .addValue("sku", it.sku)
                .addValue("price", it.price)
                .addValue("creationTimestamp", it.creationTimestamp)
                .addValue("lastUpdateTimestamp", it.lastUpdateTimestamp)
        }.toTypedArray()

        namedJdbcTemplate.batchUpdate(sql, params)
    }
}

class ProductRowMapper : RowMapper<Product> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): Product =
        Product(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            handle = rs.getString("handle"),
            productType = rs.getString("product_type"),
            creationTimestamp = rs.getTimestamp("creation_timestamp"),
            lastUpdateTimestamp = rs.getTimestamp("last_update_timestamp"),
        )
}
