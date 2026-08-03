package com.mango.products.infrastructure.adapter.out.product

import com.mango.products.domain.exception.ProductNotFoundException
import com.mango.products.domain.exception.ProductPriceNotFoundException
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import com.mango.products.infrastructure.adapter.out.product.entity.ProductEntity
import com.mango.products.infrastructure.adapter.out.product.entity.ProductPriceEntity
import com.mango.products.infrastructure.adapter.out.product.repository.ProductJpaRepository
import com.mango.products.infrastructure.adapter.out.product.repository.ProductPriceJpaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional

class ProductAdapterTest {

    private lateinit var productJpaRepository: ProductJpaRepository
    private lateinit var productPriceJpaRepository: ProductPriceJpaRepository
    private lateinit var productAdapter: ProductAdapter

    @BeforeEach
    fun setUp() {
        productJpaRepository = mockk()
        productPriceJpaRepository = mockk()
        productAdapter = ProductAdapter(productJpaRepository, productPriceJpaRepository)
    }

    @Test
    fun `should create a product`() {
        val product = productDomain(id = null)
        val savedProduct = productEntity(id = 1L)
        every { productJpaRepository.save(any<ProductEntity>()) } returns savedProduct

        val result = productAdapter.createProduct(product)

        assertThat(result).isEqualTo(productDomain(id = 1L))
        verify(exactly = 1) {
            productJpaRepository.save(
                match { it.id == null && it.name == product.name && it.description == product.description }
            )
        }
    }

    @Test
    fun `should add a price to an existing product`() {
        val productId = 1L
        val product = productEntity(id = productId)
        val price = priceDomain()
        every { productJpaRepository.findById(productId) } returns Optional.of(product)
        every { productPriceJpaRepository.save(any<ProductPriceEntity>()) } answers { firstArg() }

        assertThatCode { productAdapter.addProductPrice(productId, price) }
            .doesNotThrowAnyException()

        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 1) {
            productPriceJpaRepository.save(
                match {
                    it.product === product &&
                        it.value == price.value &&
                        it.initDate == price.initDate &&
                        it.endDate == price.endDate
                }
            )
        }
    }

    @Test
    fun `should reject adding a price when product does not exist`() {
        val productId = 1L
        every { productJpaRepository.findById(productId) } returns Optional.empty()

        assertThrows<ProductNotFoundException> {
            productAdapter.addProductPrice(productId, priceDomain())
        }

        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 0) { productPriceJpaRepository.save(any<ProductPriceEntity>()) }
    }

    @Test
    fun `should get the product price for a date`() {
        val productId = 1L
        val date = LocalDate.of(2026, 1, 15)
        val expectedPrice = BigDecimal("19.99")
        every { productJpaRepository.findById(productId) } returns Optional.of(productEntity(productId))
        every { productPriceJpaRepository.getProductPriceByDate(productId, date) } returns expectedPrice

        val result = productAdapter.getProductPriceByDate(productId, date)

        assertThat(result).isEqualByComparingTo(expectedPrice)
        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 1) { productPriceJpaRepository.getProductPriceByDate(productId, date) }
    }

    @Test
    fun `should reject getting a price when product does not exist`() {
        val productId = 1L
        val date = LocalDate.of(2026, 1, 15)
        every { productJpaRepository.findById(productId) } returns Optional.empty()

        assertThrows<ProductNotFoundException> {
            productAdapter.getProductPriceByDate(productId, date)
        }

        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 0) { productPriceJpaRepository.getProductPriceByDate(any(), any()) }
    }

    @Test
    fun `should reject getting a price when no price exists for the date`() {
        val productId = 1L
        val date = LocalDate.of(2026, 1, 15)
        every { productJpaRepository.findById(productId) } returns Optional.of(productEntity(productId))
        every { productPriceJpaRepository.getProductPriceByDate(productId, date) } returns null

        assertThrows<ProductPriceNotFoundException> {
            productAdapter.getProductPriceByDate(productId, date)
        }

        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 1) { productPriceJpaRepository.getProductPriceByDate(productId, date) }
    }

    @Test
    fun `should get the product price history`() {
        val productId = 1L
        val product = productEntity(id = productId)
        val price = priceEntity(id = 10L, product = product)
        product.prices.add(price)
        every { productJpaRepository.findById(productId) } returns Optional.of(product)

        val result = productAdapter.getProductPricesHistory(productId)

        assertThat(result).isEqualTo(productDomain(id = productId, prices = listOf(priceDomain(id = 10L))))
        verify(exactly = 1) { productJpaRepository.findById(productId) }
    }

    @Test
    fun `should reject getting price history when product does not exist`() {
        val productId = 1L
        every { productJpaRepository.findById(productId) } returns Optional.empty()

        assertThrows<ProductNotFoundException> {
            productAdapter.getProductPricesHistory(productId)
        }

        verify(exactly = 1) { productJpaRepository.findById(productId) }
    }

    @Test
    fun `should report whether a price period overlaps`() {
        val productId = 1L
        val price = priceDomain()
        every { productJpaRepository.findById(productId) } returns Optional.of(productEntity(productId))
        every {
            productPriceJpaRepository.existsOverlappingPrice(productId, price.initDate, price.endDate)
        } returns true

        val result = productAdapter.existsOverlappingPrice(productId, price)

        assertThat(result).isTrue()
        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 1) {
            productPriceJpaRepository.existsOverlappingPrice(productId, price.initDate, price.endDate)
        }
    }

    @Test
    fun `should reject checking overlaps when product does not exist`() {
        val productId = 1L
        every { productJpaRepository.findById(productId) } returns Optional.empty()

        assertThrows<ProductNotFoundException> {
            productAdapter.existsOverlappingPrice(productId, priceDomain())
        }

        verify(exactly = 1) { productJpaRepository.findById(productId) }
        verify(exactly = 0) {
            productPriceJpaRepository.existsOverlappingPrice(any(), any(), any())
        }
    }

    private fun productDomain(
        id: Long?,
        prices: List<ProductPriceDomain> = emptyList()
    ) = ProductDomain(
        id = id,
        name = "T-shirt",
        description = "Cotton T-shirt",
        prices = prices
    )

    private fun priceDomain(id: Long? = null) = ProductPriceDomain(
        id = id,
        value = BigDecimal("19.99"),
        initDate = LocalDate.of(2026, 1, 1),
        endDate = LocalDate.of(2026, 1, 31)
    )

    private fun productEntity(id: Long?) = ProductEntity(
        id = id,
        name = "T-shirt",
        description = "Cotton T-shirt"
    )

    private fun priceEntity(id: Long?, product: ProductEntity) = ProductPriceEntity(
        id = id,
        value = BigDecimal("19.99"),
        initDate = LocalDate.of(2026, 1, 1),
        endDate = LocalDate.of(2026, 1, 31),
        product = product
    )
}
