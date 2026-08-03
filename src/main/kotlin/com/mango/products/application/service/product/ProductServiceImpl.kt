package com.mango.products.application.service.product

import com.mango.products.domain.exception.InvalidPricePeriodException
import com.mango.products.domain.exception.PricePeriodOverlapException
import com.mango.products.domain.port.`in`.ProductService
import com.mango.products.domain.port.out.ProductPort
import com.mango.products.domain.product.ProductDomain
import com.mango.products.domain.product.ProductPriceDomain
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class ProductServiceImpl(private val productPort: ProductPort) : ProductService {
    override fun createProduct(productDomain: ProductDomain): ProductDomain {
        return productPort.createProduct(productDomain)
    }

    override fun addProductPrice(productId: Long, productPriceDomain: ProductPriceDomain) {
        validatePricePeriod(productPriceDomain)

        if (productPort.existsOverlappingPrice(productId, productPriceDomain)) {
            throw PricePeriodOverlapException()
        }

        productPort.addProductPrice(productId, productPriceDomain)
    }

    override fun getProductPriceByDate(productId: Long, date: LocalDate): BigDecimal {
        return productPort.getProductPriceByDate(productId, date)
    }

    override fun getProductPricesHistory(productId: Long): ProductDomain {
        return productPort.getProductPricesHistory(productId)
    }

    private fun validatePricePeriod(
        productPriceDomain: ProductPriceDomain
    ) {
        if (
            productPriceDomain.endDate != null &&
            !productPriceDomain.endDate.isAfter(productPriceDomain.initDate)
        ) {
            throw InvalidPricePeriodException(productPriceDomain.initDate, productPriceDomain.endDate)
        }
    }
}