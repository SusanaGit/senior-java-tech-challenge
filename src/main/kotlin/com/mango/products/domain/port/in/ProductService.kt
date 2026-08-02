package com.mango.products.domain.port.`in`

import com.mango.products.domain.product.ProductDomain

interface ProductService {
    fun createProduct(productDomain: ProductDomain)
}