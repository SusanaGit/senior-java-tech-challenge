package com.mango.products.infrastructure.adapter.`in`.exception

import com.mango.products.domain.exception.InvalidPricePeriodException
import com.mango.products.domain.exception.PricePeriodOverlapException
import com.mango.products.domain.exception.ProductNotFoundException
import com.mango.products.domain.exception.ProductPriceNotFoundException
import com.mango.products.infrastructure.api.model.ApiError
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDate

class ApiExceptionHandlerTest {

    private val exceptionHandler = ApiExceptionHandler()

    @Test
    fun `should return not found when product does not exist`() {
        val productId = 1L
        val exception = ProductNotFoundException(productId)
        val expectedResponse = response(
            status = HttpStatus.NOT_FOUND,
            message = "Product with id $productId was not found."
        )

        val result = exceptionHandler.handleProductNotFound(exception)

        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return bad request when price period is invalid`() {
        val initDate = LocalDate.of(2026, 1, 31)
        val endDate = LocalDate.of(2026, 1, 1)
        val exception = InvalidPricePeriodException(initDate, endDate)
        val expectedResponse = response(
            status = HttpStatus.BAD_REQUEST,
            message = "endDate $endDate must be later than initDate $initDate"
        )

        val result = exceptionHandler.handleInvalidPricePeriod(exception)

        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return conflict when price period overlaps`() {
        val exception = PricePeriodOverlapException()
        val expectedResponse = response(
            status = HttpStatus.CONFLICT,
            message = "The price period overlaps with an existing price period."
        )

        val result = exceptionHandler.handlePricePeriodOverlap(exception)

        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return not found when product price does not exist for date`() {
        val productId = 1L
        val date = LocalDate.of(2026, 1, 15)
        val exception = ProductPriceNotFoundException(productId, date)
        val expectedResponse = response(
            status = HttpStatus.NOT_FOUND,
            message = "No price found for product with id $productId on date $date."
        )

        val result = exceptionHandler.handleProductPriceNotFound(exception)

        assertThat(result).isEqualTo(expectedResponse)
    }

    private fun response(status: HttpStatus, message: String): ResponseEntity<ApiError> =
        ResponseEntity.status(status).body(
            ApiError(
                status = status.value(),
                error = status.reasonPhrase,
                message = message
            )
        )
}
