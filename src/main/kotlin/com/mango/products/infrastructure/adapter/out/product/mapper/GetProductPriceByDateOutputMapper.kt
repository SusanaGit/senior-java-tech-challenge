package com.mango.products.infrastructure.adapter.out.product.mapper

import com.mango.products.infrastructure.api.model.GetProductPriceByDateOutput
import java.math.BigDecimal

fun BigDecimal.toGetProductPriceByDateOutput(): GetProductPriceByDateOutput =
    GetProductPriceByDateOutput(
        value = this
    )