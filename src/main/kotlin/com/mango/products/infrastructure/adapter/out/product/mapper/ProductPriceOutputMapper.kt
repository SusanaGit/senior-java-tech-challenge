package com.mango.products.infrastructure.adapter.out.product.mapper

import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.api.model.ProductPriceOutput

fun ProductPriceDomain.toProductPriceOutput() =
    ProductPriceOutput(
        value = value,
        initDate = initDate,
        endDate = endDate
    )