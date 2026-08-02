package com.mango.products.infrastructure.adapter.`in`.exception

import com.mango.products.domain.exception.InvalidPricePeriodException
import com.mango.products.domain.exception.PricePeriodOverlapException
import com.mango.products.domain.exception.ProductNotFoundException
import com.mango.products.domain.exception.ProductPriceNotFoundException
import com.mango.products.infrastructure.api.model.ApiError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(
        exception: ProductNotFoundException
    ): ResponseEntity<ApiError> {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError(
                status = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.reasonPhrase,
                message = requireNotNull(exception.message)
            )
        )
    }

    @ExceptionHandler(InvalidPricePeriodException::class)
    fun handleInvalidPricePeriod(
        exception: InvalidPricePeriodException
    ): ResponseEntity<ApiError> {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiError(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = requireNotNull(exception.message)
            )
        )
    }

    @ExceptionHandler(PricePeriodOverlapException::class)
    fun handlePricePeriodOverlap(
        exception: PricePeriodOverlapException
    ): ResponseEntity<ApiError> {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError(
                status = HttpStatus.CONFLICT.value(),
                error = HttpStatus.CONFLICT.reasonPhrase,
                message = requireNotNull(exception.message)
            )
        )
    }

    @ExceptionHandler(ProductPriceNotFoundException::class)
    fun handleProductPriceNotFound(
        exception: ProductPriceNotFoundException
    ): ResponseEntity<ApiError> {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError(
                status = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.reasonPhrase,
                message = requireNotNull(exception.message)
            )
        )
    }
}