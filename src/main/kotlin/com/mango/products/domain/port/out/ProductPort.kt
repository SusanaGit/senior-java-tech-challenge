package com.mango.products.domain.port.out

import com.mango.products.domain.product.ProductDomain

interface ProductPort {
    fun createProduct(productDomain: ProductDomain)
}