package com.mango.products.infrastructure.adapter.out.product.entity.mapper

import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.adapter.out.product.entity.ProductEntity
import com.mango.products.infrastructure.adapter.out.product.entity.ProductPriceEntity

fun ProductPriceDomain.toEntity(productEntity: ProductEntity): ProductPriceEntity =
    ProductPriceEntity(
        id = id,
        value = value,
        initDate = initDate,
        endDate = endDate,
        product = productEntity
    )

fun ProductPriceEntity.toDomain(): ProductPriceDomain =
    ProductPriceDomain(
        id = id,
        value = value,
        initDate = initDate,
        endDate = endDate
    )