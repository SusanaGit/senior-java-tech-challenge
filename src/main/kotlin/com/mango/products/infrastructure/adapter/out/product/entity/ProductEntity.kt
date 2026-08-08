package com.mango.products.infrastructure.adapter.out.product.entity

import jakarta.persistence.*

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

    @Column(nullable = false, length = 255)
    var name: String,

    @Column(nullable = false, length = 255)
    var description: String,

    @OneToMany(
        mappedBy = "product",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var prices: MutableList<ProductPriceEntity> = mutableListOf()
)