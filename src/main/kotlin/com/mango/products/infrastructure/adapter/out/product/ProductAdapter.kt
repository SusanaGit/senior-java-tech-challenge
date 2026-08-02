package com.mango.products.infrastructure.adapter.out.product

import com.mango.products.domain.port.out.ProductPort
import com.mango.products.domain.product.ProductDomain
import com.mango.products.infrastructure.adapter.out.product.mapper.toEntity
import com.mango.products.infrastructure.adapter.out.product.repository.ProductJpaRepository
import org.springframework.stereotype.Component

@Component
class ProductAdapter(private val productJpaRepository: ProductJpaRepository) : ProductPort {
    override fun createProduct(productDomain: ProductDomain) {
        productJpaRepository.save(productDomain.toEntity())
    }
}