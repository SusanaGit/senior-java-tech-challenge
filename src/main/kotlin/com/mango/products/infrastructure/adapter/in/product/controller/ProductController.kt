package com.mango.products.infrastructure.adapter.`in`.product.controller

import com.mango.products.domain.port.`in`.ProductService
import com.mango.products.infrastructure.adapter.`in`.product.mapper.toDomain
import com.mango.products.infrastructure.api.ProductsApi
import com.mango.products.infrastructure.api.model.AddProductPriceInput
import com.mango.products.infrastructure.api.model.CreateProductInput
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productService: ProductService) : ProductsApi {
    override fun createProduct(
        createProductInput: CreateProductInput
    ): ResponseEntity<Unit> {
        productService.createProduct(
            createProductInput.toDomain()
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    override fun addProductPrice(
        productId: Long,
        addProductPriceInput: AddProductPriceInput
    ): ResponseEntity<Unit> {
        productService.addProductPrice(
            productId,
            addProductPriceInput.toDomain()
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    /*override fun getProductPriceHistory(
        id: Long
    ): ResponseEntity<ProductPriceHistory> {
        TODO("Not yet implemented")
    }

    override fun getProductPriceByDate(
        id: Long,
        date: LocalDate
    ): ResponseEntity<ApplicablePrice> {
        TODO("Not yet implemented")
    }*/

}