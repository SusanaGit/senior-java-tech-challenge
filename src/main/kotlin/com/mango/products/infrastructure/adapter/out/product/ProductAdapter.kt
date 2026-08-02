package com.mango.products.infrastructure.adapter.out.product

import com.mango.products.domain.port.out.ProductPort
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.adapter.out.product.entity.mapper.toDomain
import com.mango.products.infrastructure.adapter.out.product.entity.mapper.toEntity
import com.mango.products.infrastructure.adapter.out.product.repository.ProductJpaRepository
import com.mango.products.infrastructure.adapter.out.product.repository.ProductPriceJpaRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class ProductAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productPriceJpaRepository: ProductPriceJpaRepository
) : ProductPort {
    override fun createProduct(productDomain: ProductDomain) {
        productJpaRepository.save(productDomain.toEntity())
    }

    override fun addProductPrice(
        productId: Long,
        productPriceDomain: ProductPriceDomain
    ) {
        val productEntity = productJpaRepository.findById(productId)
            .orElseThrow {
                RuntimeException("Product with id: $productId not found")
            }

        productPriceJpaRepository.save(
            productPriceDomain.toEntity(productEntity)
        )
    }

    override fun getProductPriceByDate(productId: Long, date: LocalDate): BigDecimal {
        val price = productPriceJpaRepository
            .getProductPriceByDate(productId, date)
            ?: throw RuntimeException("Product with id: $productId not found")
            //?: throw ProductPriceNotFoundException(productId, date)

        return price.value
    }

    override fun getProductPricesHistory(productId: Long): ProductDomain {
        val productEntity = productJpaRepository.findById(productId)
            .orElseThrow {
                RuntimeException("Product not found: $productId")
            }

        return productEntity.toDomain(productPriceJpaRepository.findAllByProductIdOrderByInitDateAsc(productId))
    }
}