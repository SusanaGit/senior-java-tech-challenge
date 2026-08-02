package com.mango.products.infrastructure.adapter.out.product.mapper

import com.mango.products.domain.product.ProductDomain
import com.mango.products.infrastructure.api.model.GetProductPricesHistoryOutput

fun ProductDomain.toGetProductPricesHistoryOutput() =
    GetProductPricesHistoryOutput(
        name = name,
        description = description,
        prices = prices.map { it.toProductPriceOutput() }
    )