package com.mango.products.application.service.product

import com.mango.products.domain.exception.PricePeriodOverlapException
import com.mango.products.domain.port.out.ProductPort
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class ProductServiceImplTest {

    private lateinit var productPort: ProductPort
    private lateinit var productService: ProductServiceImpl

    @BeforeEach
    fun setUp() {
        productPort = mockk()
        productService = ProductServiceImpl(productPort)
    }

    @Test
    fun `should create a product`() {
        val product = product()
        val createdProduct = product.copy(id = 1L)
        every { productPort.createProduct(product) } returns createdProduct

        val result = productService.createProduct(product)

        assertThat(result).isEqualTo(createdProduct)
        verify(exactly = 1) { productPort.createProduct(product) }
    }

    @Test
    fun `should add a product price when its period does not overlap`() {
        val productId = 1L
        val price = price()
        every { productPort.existsOverlappingPrice(productId, price) } returns false
        every { productPort.addProductPrice(productId, price) } just runs

        assertThatCode { productService.addProductPrice(productId, price) }
            .doesNotThrowAnyException()

        verify(exactly = 1) { productPort.existsOverlappingPrice(productId, price) }
        verify(exactly = 1) { productPort.addProductPrice(productId, price) }
    }

    @Test
    fun `should add a product price with no end date`() {
        val productId = 1L
        val openEndedPrice = price().copy(endDate = null)
        every { productPort.existsOverlappingPrice(productId, openEndedPrice) } returns false
        every { productPort.addProductPrice(productId, openEndedPrice) } just runs

        assertThatCode { productService.addProductPrice(productId, openEndedPrice) }
            .doesNotThrowAnyException()

        verify(exactly = 1) { productPort.existsOverlappingPrice(productId, openEndedPrice) }
        verify(exactly = 1) { productPort.addProductPrice(productId, openEndedPrice) }
    }

    @Test
    fun `should reject a product price when its period overlaps`() {
        val productId = 1L
        val price = price()
        every { productPort.existsOverlappingPrice(productId, price) } returns true

        assertThatThrownBy { productService.addProductPrice(productId, price) }
            .isInstanceOf(PricePeriodOverlapException::class.java)

        verify(exactly = 1) { productPort.existsOverlappingPrice(productId, price) }
        verify(exactly = 0) { productPort.addProductPrice(any(), any()) }
    }

    @Test
    fun `should get the product price for a date`() {
        val productId = 1L
        val date = LocalDate.of(2026, 1, 15)
        val expectedPrice = BigDecimal("19.99")
        every { productPort.getProductPriceByDate(productId, date) } returns expectedPrice

        val result = productService.getProductPriceByDate(productId, date)

        assertThat(result).isEqualByComparingTo(expectedPrice)
        verify(exactly = 1) { productPort.getProductPriceByDate(productId, date) }
    }

    @Test
    fun `should get the product price history`() {
        val productId = 1L
        val expectedProduct = product().copy(id = productId, prices = listOf(price()))
        every { productPort.getProductPricesHistory(productId) } returns expectedProduct

        val result = productService.getProductPricesHistory(productId)

        assertThat(result).isEqualTo(expectedProduct)
        verify(exactly = 1) { productPort.getProductPricesHistory(productId) }
    }

    private fun product() = ProductDomain(
        id = null,
        name = "T-shirt",
        description = "Cotton T-shirt"
    )

    private fun price() = ProductPriceDomain(
        id = null,
        value = BigDecimal("19.99"),
        initDate = LocalDate.of(2026, 1, 1),
        endDate = LocalDate.of(2026, 1, 31)
    )
}
