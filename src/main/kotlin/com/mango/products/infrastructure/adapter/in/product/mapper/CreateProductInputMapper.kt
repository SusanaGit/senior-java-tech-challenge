package com.mango.products.infrastructure.adapter.`in`.product.mapper

import com.mango.products.domain.product.ProductDomain
import com.mango.products.infrastructure.api.model.CreateProductInput

fun CreateProductInput.toDomain(): ProductDomain =
    ProductDomain(
        id = null,
        name = name,
        description = description
    )
