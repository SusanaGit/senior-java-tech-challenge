package com.mango.products.application.service.product

import com.mango.products.domain.port.`in`.ProductService
import com.mango.products.domain.port.out.ProductPort
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(private val productPort: ProductPort) : ProductService {
    override fun createProduct(productDomain: ProductDomain) {
        return productPort.createProduct(productDomain)
    }

    override fun addProductPrice(productId: Long, productPriceDomain: ProductPriceDomain) {
        return productPort.addProductPrice(productId, productPriceDomain)
    }
}