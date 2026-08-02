package com.mango.products.infrastructure.adapter.out.product.repository

import com.mango.products.infrastructure.adapter.out.product.entity.ProductPriceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductPriceJpaRepository : JpaRepository<ProductPriceEntity, Long>