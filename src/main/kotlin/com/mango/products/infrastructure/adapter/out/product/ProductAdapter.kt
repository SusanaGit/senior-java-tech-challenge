package com.mango.products.infrastructure.adapter.out.product

import com.mango.products.domain.exception.ProductNotFoundException
import com.mango.products.domain.exception.ProductPriceNotFoundException
import com.mango.products.domain.port.out.ProductPort
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.adapter.out.product.entity.ProductEntity
import com.mango.products.infrastructure.adapter.out.product.entity.mapper.toDomain
import com.mango.products.infrastructure.adapter.out.product.entity.mapper.toEntity
import com.mango.products.infrastructure.adapter.out.product.repository.ProductJpaRepository
import com.mango.products.infrastructure.adapter.out.product.repository.ProductPriceJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Component
class ProductAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productPriceJpaRepository: ProductPriceJpaRepository
) : ProductPort {

    override fun createProduct(productDomain: ProductDomain): ProductDomain =
        productJpaRepository.save(productDomain.toEntity()).toDomain()

    override fun addProductPrice(
        productId: Long,
        productPriceDomain: ProductPriceDomain
    ) {
        productPriceJpaRepository.save(
            productPriceDomain.toEntity(findProductEntity(productId))
        )
    }

    override fun getProductPriceByDate(productId: Long, date: LocalDate): BigDecimal {
        findProductEntity(productId)

        return productPriceJpaRepository
            .getProductPriceByDate(productId, date)
            ?: throw ProductPriceNotFoundException(productId, date)
    }

    @Transactional(readOnly = true)
    override fun getProductPricesHistory(productId: Long): ProductDomain =
        findProductEntity(productId).toDomain()

    override fun existsOverlappingPrice(
        productId: Long,
        productPriceDomain: ProductPriceDomain
    ): Boolean {
        findProductEntity(productId)
        return productPriceJpaRepository.existsOverlappingPrice(
            productId = productId,
            initDate = productPriceDomain.initDate,
            endDate = productPriceDomain.endDate
        )
    }

    private fun findProductEntity(productId: Long): ProductEntity {
        return productJpaRepository.findById(productId)
            .orElseThrow {
                ProductNotFoundException(productId)
            }
    }
}