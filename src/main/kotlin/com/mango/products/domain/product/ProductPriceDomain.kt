package com.mango.products.domain.product

import java.math.BigDecimal
import java.time.LocalDate

data class ProductPriceDomain(
    val id: Long?,
    val value: BigDecimal,
    val initDate: LocalDate,
    val endDate: LocalDate?,
)