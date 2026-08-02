package com.mango.products.domain.exception

class ProductNotFoundException(
    productId: Long
) : RuntimeException(
    "Product with id $productId was not found."
)