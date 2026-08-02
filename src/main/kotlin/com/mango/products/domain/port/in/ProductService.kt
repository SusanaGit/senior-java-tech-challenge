package com.mango.products.domain.port.`in`

import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain

interface ProductService {
    fun createProduct(productDomain: ProductDomain)
    fun addProductPrice(productId: Long, productPriceDomain: ProductPriceDomain)
}