package com.mango.products.infrastructure.adapter.`in`.product.controller

import com.mango.products.domain.port.`in`.ProductService
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.api.model.AddProductPriceInput
import com.mango.products.infrastructure.api.model.CreateProductInput
import com.mango.products.infrastructure.api.model.CreateProductOutput
import com.mango.products.infrastructure.api.model.GetProductPriceByDateOutput
import com.mango.products.infrastructure.api.model.GetProductPricesHistoryOutput
import com.mango.products.infrastructure.api.model.ProductPriceOutput
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.time.LocalDate

class ProductControllerTest {

    private lateinit var productService: ProductService
    private lateinit var productController: ProductController

    @BeforeEach
    fun setUp() {
        productService = mockk()
        productController = ProductController(productService)
    }

    @Test
    fun `should create a product`() {
        val input = CreateProductInput(
            name = "T-shirt",
            description = "Cotton T-shirt"
        )
        val productToCreate = product(id = null)
        val createdProduct = product(id = 1L)
        val expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(
            CreateProductOutput(
                id = 1L,
                name = createdProduct.name,
                description = createdProduct.description
            )
        )
        every { productService.createProduct(productToCreate) } returns createdProduct

        val result = productController.createProduct(input)

        assertThat(result).isEqualTo(expectedResponse)
        verify(exactly = 1) { productService.createProduct(productToCreate) }
    }

    @Test
    fun `should add a price to a product`() {
        val productId = 1L
        val price = price()
        val input = AddProductPriceInput(
            value = price.value,
            initDate = price.initDate,
            endDate = price.endDate
        )
        val expectedResponse = ResponseEntity.status(HttpStatus.CREATED).build<Unit>()
        every { productService.addProductPrice(productId, price) } just runs

        val result = productController.addProductPrice(productId, input)

        assertThat(result).isEqualTo(expectedResponse)
        verify(exactly = 1) { productService.addProductPrice(productId, price) }
    }

    @Test
    fun `should get the product price for a date`() {
        val productId = 1L
        val date = LocalDate.of(2026, 1, 15)
        val priceValue = BigDecimal("19.99")
        val expectedResponse = ResponseEntity.ok(
            GetProductPriceByDateOutput(value = priceValue)
        )
        every { productService.getProductPriceByDate(productId, date) } returns priceValue

        val result = productController.getProductPriceByDate(productId, date)

        assertThat(result).isEqualTo(expectedResponse)
        verify(exactly = 1) { productService.getProductPriceByDate(productId, date) }
    }

    @Test
    fun `should get the product price history`() {
        val productId = 1L
        val price = price()
        val product = product(id = productId, prices = listOf(price))
        val expectedResponse = ResponseEntity.ok(
            GetProductPricesHistoryOutput(
                name = product.name,
                description = product.description,
                prices = listOf(
                    ProductPriceOutput(
                        value = price.value,
                        initDate = price.initDate,
                        endDate = price.endDate
                    )
                )
            )
        )
        every { productService.getProductPricesHistory(productId) } returns product

        val result = productController.getProductPricesHistory(productId)

        assertThat(result).isEqualTo(expectedResponse)
        verify(exactly = 1) { productService.getProductPricesHistory(productId) }
    }

    private fun product(
        id: Long?,
        prices: List<ProductPriceDomain> = emptyList()
    ) = ProductDomain(
        id = id,
        name = "T-shirt",
        description = "Cotton T-shirt",
        prices = prices
    )

    private fun price() = ProductPriceDomain(
        id = null,
        value = BigDecimal("19.99"),
        initDate = LocalDate.of(2026, 1, 1),
        endDate = LocalDate.of(2026, 1, 31)
    )
}
