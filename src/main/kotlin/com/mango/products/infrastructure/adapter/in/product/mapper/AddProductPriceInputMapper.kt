package com.mango.products.infrastructure.adapter.`in`.product.mapper

import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.api.model.AddProductPriceInput

fun AddProductPriceInput.toDomain(): ProductPriceDomain =
    ProductPriceDomain(
        id = null,
        value = value,
        initDate = initDate,
        endDate = endDate
    )