package com.mango.products.domain.product

import com.mango.products.domain.exception.InvalidPricePeriodException
import java.math.BigDecimal
import java.time.LocalDate

data class ProductPriceDomain(
    val id: Long?,
    val value: BigDecimal,
    val initDate: LocalDate,
    val endDate: LocalDate?,
){
    init {
        if (endDate != null && !endDate.isAfter(initDate)) {
            throw InvalidPricePeriodException(initDate, endDate)
        }
    }
}