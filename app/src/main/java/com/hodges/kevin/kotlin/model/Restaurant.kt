package com.hodges.kevin.kotlin.model

import com.hodges.kevin.kotlin.enums.RestaurantPriceEnum

/**
 * Created by Kevin on 2/7/2018.
 */
data class Restaurant(val name: String, val priceCategory: RestaurantPriceEnum, var isCurrentlySelected: Boolean = false)



