package com.mango.products.infrastructure.adapter.`in`.product.controller

import com.mango.products.domain.port.`in`.ProductService
import com.mango.products.infrastructure.adapter.`in`.product.mapper.toDomain
import com.mango.products.infrastructure.adapter.out.product.mapper.toCreateProductOutput
import com.mango.products.infrastructure.adapter.out.product.mapper.toGetProductPriceByDateOutput
import com.mango.products.infrastructure.adapter.out.product.mapper.toGetProductPricesHistoryOutput
import com.mango.products.infrastructure.api.ProductsApi
import com.mango.products.infrastructure.api.model.GetProductPriceByDateOutput
import com.mango.products.infrastructure.api.model.AddProductPriceInput
import com.mango.products.infrastructure.api.model.CreateProductInput
import com.mango.products.infrastructure.api.model.CreateProductOutput
import com.mango.products.infrastructure.api.model.GetProductPricesHistoryOutput
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class ProductController(private val productService: ProductService) : ProductsApi {
    override fun createProduct(
        createProductInput: CreateProductInput
    ): ResponseEntity<CreateProductOutput> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                productService
                    .createProduct(createProductInput.toDomain())
                    .toCreateProductOutput()
            )

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

    override fun getProductPriceByDate(
        productId: Long,
        date: LocalDate
    ): ResponseEntity<GetProductPriceByDateOutput> {
        return ResponseEntity.ok(productService.getProductPriceByDate(productId, date).toGetProductPriceByDateOutput())
    }

    override fun getProductPricesHistory(
        productId: Long
    ): ResponseEntity<GetProductPricesHistoryOutput> {
        return ResponseEntity.ok(productService.getProductPricesHistory(productId).toGetProductPricesHistoryOutput())
    }
}