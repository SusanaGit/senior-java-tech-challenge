package com.mango.products.infrastructure.adapter.out.product.entity.mapper

import com.mango.products.domain.product.ProductDomain
import com.mango.products.infrastructure.adapter.out.product.entity.ProductEntity

fun ProductDomain.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        name = name,
        description = description,
        prices = mutableListOf()
    )

fun ProductEntity.toDomain(): ProductDomain =
    ProductDomain(
        id = id,
        name = name,
        description = description,
        prices = prices.map { it.toDomain() }
    )