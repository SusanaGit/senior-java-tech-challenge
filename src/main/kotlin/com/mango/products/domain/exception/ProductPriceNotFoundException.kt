package com.mango.products.domain.exception

import java.time.LocalDate

class ProductPriceNotFoundException(
    productId: Long,
    date: LocalDate
) : RuntimeException(
    "No price found for product with id $productId on date $date."
)