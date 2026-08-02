package com.mango.products.infrastructure.adapter.out.product.repository

import com.mango.products.infrastructure.adapter.out.product.entity.PriceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PriceJpaRepository : JpaRepository<PriceEntity, Long>