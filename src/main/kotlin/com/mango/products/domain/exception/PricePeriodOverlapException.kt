package com.mango.products.domain.exception

class PricePeriodOverlapException :
    RuntimeException("The price period overlaps with an existing price period.")