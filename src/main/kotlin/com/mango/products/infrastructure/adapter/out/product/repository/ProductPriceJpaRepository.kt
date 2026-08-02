package com.mango.products.infrastructure.adapter.out.product.repository

import com.mango.products.infrastructure.adapter.out.product.entity.ProductPriceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.time.LocalDate

interface ProductPriceJpaRepository : JpaRepository<ProductPriceEntity, Long> {

    @Query("""
    SELECT p.value
    FROM ProductPriceEntity p
    WHERE p.product.id = :productId
      AND p.initDate <= :date
      AND (p.endDate IS NULL OR p.endDate >= :date)
    """)
    fun getProductPriceByDate(
        @Param("productId") productId: Long,
        @Param("date") date: LocalDate
    ): BigDecimal?

    fun findAllByProductIdOrderByInitDateAsc(
        productId: Long
    ): List<ProductPriceEntity>

    @Query(
        """
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
        FROM ProductPriceEntity p
        WHERE p.product.id = :productId
          AND (:endDate IS NULL OR p.initDate < :endDate)
          AND (p.endDate IS NULL OR p.endDate > :initDate)
        """
    )
    fun existsOverlappingPrice(
        @Param("productId") productId: Long,
        @Param("initDate") initDate: LocalDate,
        @Param("endDate") endDate: LocalDate?
    ): Boolean
}