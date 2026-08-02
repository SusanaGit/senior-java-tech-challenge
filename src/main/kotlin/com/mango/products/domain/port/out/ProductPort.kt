package com.mango.products.domain.port.out

import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain

interface ProductPort {
    fun createProduct(productDomain: ProductDomain)
    fun addProductPrice(productId: Long, productPriceDomain: ProductPriceDomain)
}