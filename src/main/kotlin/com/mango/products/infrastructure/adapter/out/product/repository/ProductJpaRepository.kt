package com.mango.products.infrastructure.adapter.out.product.repository

import com.mango.products.infrastructure.adapter.out.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductEntity, Long>