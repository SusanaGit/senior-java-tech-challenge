package com.mango.products.domain.product

data class CreateProductDomain(
    val id: Long?,
    val name: String,
    val description: String
)