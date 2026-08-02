package com.mango.products.domain.port.`in`

import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import java.math.BigDecimal
import java.time.LocalDate

interface ProductService {
    fun createProduct(productDomain: ProductDomain)
    fun addProductPrice(productId: Long, productPriceDomain: ProductPriceDomain)
    fun getProductPriceByDate(productId: Long, date: LocalDate): BigDecimal
}