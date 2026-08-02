package com.mango.products.domain.product

data class ProductDomain (
    val id: Long?,
    val name: String,
    val description: String,
    val prices: List<PriceDomain> = emptyList()
)