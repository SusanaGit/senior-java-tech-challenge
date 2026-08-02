package com.mango.products.domain.port.out

import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import java.math.BigDecimal
import java.time.LocalDate

interface ProductPort {
    fun createProduct(productDomain: ProductDomain)
    fun addProductPrice(productId: Long, productPriceDomain: ProductPriceDomain)
    fun getProductPriceByDate(productId: Long, date: LocalDate): BigDecimal
    fun getProductPricesHistory(productId: Long): ProductDomain
}