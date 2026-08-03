package com.mango.products.domain.product

import com.mango.products.domain.exception.InvalidPricePeriodException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate

class ProductPriceDomainTest {

    @Test
    fun `should create a price when end date is after initial date`() {
        val initDate = LocalDate.of(2026, 1, 1)
        val endDate = LocalDate.of(2026, 1, 31)

        val result = price(initDate = initDate, endDate = endDate)

        assertThat(result)
            .isEqualTo(price(initDate = initDate, endDate = endDate))
    }

    @Test
    fun `should create a price with no end date`() {
        assertThatCode { price(endDate = null) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `should reject a price when end date equals initial date`() {
        val date = LocalDate.of(2026, 1, 1)

        assertThrows<InvalidPricePeriodException> {
            price(initDate = date, endDate = date)
        }
    }

    @Test
    fun `should reject a price when end date is before initial date`() {
        val initDate = LocalDate.of(2026, 1, 2)
        val endDate = LocalDate.of(2026, 1, 1)

        assertThrows<InvalidPricePeriodException> {
            price(initDate = initDate, endDate = endDate)
        }
    }

    private fun price(
        initDate: LocalDate = LocalDate.of(2026, 1, 1),
        endDate: LocalDate? = LocalDate.of(2026, 1, 31)
    ) = ProductPriceDomain(
        id = null,
        value = BigDecimal("19.99"),
        initDate = initDate,
        endDate = endDate
    )
}
