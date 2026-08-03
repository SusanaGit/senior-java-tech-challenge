package com.mango.products.infrastructure.adapter.out.product.mapper

import com.mango.products.domain.product.ProductDomain
import com.mango.products.infrastructure.api.model.CreateProductOutput

fun ProductDomain.toCreateProductOutput(): CreateProductOutput =
    CreateProductOutput(
        id = requireNotNull(id),
        name = name,
        description = description
    )
