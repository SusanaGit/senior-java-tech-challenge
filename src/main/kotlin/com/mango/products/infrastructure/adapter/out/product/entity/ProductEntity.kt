package com.mango.products.infrastructure.adapter.out.product.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "PRODUCT")
class ProductEntity(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "product_sequence"
    )
    @SequenceGenerator(
        name = "product_sequence",
        sequenceName = "PRODUCT_SEQUENCE",
        allocationSize = 1
    )
    var id: Long? = null,

    var name: String,

    var description: String,

    @OneToMany(
        mappedBy = "productId",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var prices: MutableList<ProductPriceEntity> = mutableListOf()
)