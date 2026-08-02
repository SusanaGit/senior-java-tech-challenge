package com.mango.products.domain.product

import java.math.BigDecimal
import java.util.Date

data class PriceDomain(
    val value: BigDecimal,
    val initDate: Date,
    val endDate: Date
)