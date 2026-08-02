package com.mango.products.domain.exception

import java.time.LocalDate

class InvalidPricePeriodException(
    initDate: LocalDate,
    endDate: LocalDate
) : RuntimeException(
    "endDate $endDate must be later than initDate $initDate"
)