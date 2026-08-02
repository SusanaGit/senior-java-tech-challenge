package com.mango.products.infrastructure.adapter.out.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "PRICE")
class ProductPriceEntity(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "price_sequence"
    )
    @SequenceGenerator(
        name = "price_sequence",
        sequenceName = "PRICE_SEQUENCE",
        allocationSize = 1
    )
    var id: Long? = null,

    @Column(nullable = false, precision = 12, scale = 2)
    var value: BigDecimal,

    @Column(name = "INIT_DATE", nullable = false)
    var initDate: LocalDate,

    @Column(name = "END_DATE")
    var endDate: LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    var productId: ProductEntity
)