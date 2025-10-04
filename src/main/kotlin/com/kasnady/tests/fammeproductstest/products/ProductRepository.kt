package com.kasnady.tests.fammeproductstest.products

import com.kasnady.tests.fammeproductstest.products.models.Product
import com.kasnady.tests.fammeproductstest.products.models.ProductVariant
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.sql.Timestamp

interface ProductRepository {
    fun getProducts(): List<Product>

    fun searchProductsByTitle(searchTerm: String): List<Product>

    fun saveProduct(product: ProductRequest): Long

    fun saveProducts(products: List<Product>)

    fun saveVariants(productId: Long, variants: List<ProductVariantRequest>)

    fun saveVariants(variants: List<ProductVariant>)
}

@Repository
@Transactional
class ProductRepositoryImpl(
    private val namedJdbcTemplate: NamedParameterJdbcTemplate,
): ProductRepository {

    override fun getProducts(): List<Product> {
        val contentQuery = """
            WITH limited_products AS (
                SELECT * FROM product
                LIMIT 100
            )
            
            SELECT
                pv.product_id,
                lp.title as product_title,
                lp.handle as product_handle,
                lp.product_type,
                lp.creation_timestamp,
                lp.last_update_timestamp,
                pv.id as variant_id,
                pv.title as variant_title,
                pv.sku,
                pv.price
            FROM limited_products lp
            LEFT JOIN product_variant pv ON pv.product_id = lp.id
        """.trimIndent()

        val productRaws = namedJdbcTemplate.query(contentQuery, ProductRawRowMapper())

        return productRaws
            .groupBy { it.productId }
            .map { (_, raws) ->
                val first = raws.first()
                Product(
                    id = first.productId,
                    title = first.productTitle,
                    handle = first.productHandle,
                    productType = first.productType,
                    creationTimestamp = first.creationTimestamp,
                    lastUpdateTimestamp = first.lastUpdateTimestamp,
                    variants = raws.map {
                        ProductVariant(
                            id = it.variantId,
                            productId = it.productId,
                            title = it.variantTitle,
                            sku = it.sku,
                            price = it.price,
                            creationTimestamp = first.creationTimestamp,
                            lastUpdateTimestamp = first.lastUpdateTimestamp,
                        )
                    }
                )
            }
    }

    override fun searchProductsByTitle(searchTerm: String): List<Product> {
        val contentQuery = """
            WITH limited_products AS (
                SELECT * FROM product
                WHERE LOWER(title) LIKE LOWER(:searchTerm)
                LIMIT 10
            )
            
            SELECT
                pv.product_id,
                lp.title as product_title,
                lp.handle as product_handle,
                lp.product_type,
                lp.creation_timestamp,
                lp.last_update_timestamp,
                pv.id as variant_id,
                pv.title as variant_title,
                pv.sku,
                pv.price
            FROM limited_products lp
            LEFT JOIN product_variant pv ON pv.product_id = lp.id
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("searchTerm", "%$searchTerm%")

        val productRaws = namedJdbcTemplate.query(contentQuery, params, ProductRawRowMapper())

        return productRaws
            .groupBy { it.productId }
            .map { (_, raws) ->
                val first = raws.first()
                Product(
                    id = first.productId,
                    title = first.productTitle,
                    handle = first.productHandle,
                    productType = first.productType,
                    creationTimestamp = first.creationTimestamp,
                    lastUpdateTimestamp = first.lastUpdateTimestamp,
                    variants = raws.map {
                        ProductVariant(
                            id = it.variantId,
                            productId = it.productId,
                            title = it.variantTitle,
                            sku = it.sku,
                            price = it.price,
                            creationTimestamp = first.creationTimestamp,
                            lastUpdateTimestamp = first.lastUpdateTimestamp,
                        )
                    }
                )
            }
    }

    override fun saveProduct(product: ProductRequest): Long {
        val keyHolder = GeneratedKeyHolder()

        val sql = """
            INSERT INTO product (
                title, handle, product_type,
                creation_timestamp, last_update_timestamp
            ) VALUES (
                :title, :handle, :productType,
                NOW(), NOW()
            )
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", product.title)
            .addValue("handle", product.handle)
            .addValue("productType", product.productType)

        namedJdbcTemplate.update(
            sql,
            params,
            keyHolder,
            arrayOf("id"),
        )

        return keyHolder.key as Long
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

    override fun saveVariants(productId: Long, variants: List<ProductVariantRequest>) {
        if (variants.isEmpty()) return

        val sql = """
            INSERT INTO product_variant (
                product_id, title, sku, price,
                creation_timestamp, last_update_timestamp
            ) VALUES (
                :productId, :title, :sku, :price,
                NOW(), NOW()
            )
        """.trimIndent()

        val params = variants.map {
            MapSqlParameterSource()
                .addValue("productId", productId)
                .addValue("title", it.title)
                .addValue("sku", it.sku)
                .addValue("price", it.price)
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

class ProductRawRowMapper : RowMapper<ProductRaw> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): ProductRaw = ProductRaw(
        productId = rs.getLong("product_id"),
        productTitle = rs.getString("product_title"),
        productHandle = rs.getString("product_handle"),
        productType = rs.getString("product_type"),
        creationTimestamp = rs.getTimestamp("creation_timestamp"),
        lastUpdateTimestamp = rs.getTimestamp("last_update_timestamp"),
        variantId = rs.getLong("variant_id"),
        variantTitle = rs.getString("variant_title"),
        sku = rs.getString("sku"),
        price = rs.getString("price"),
    )
}
