package com.hodges.kevin.kotlin.viewmodel

import android.content.Context
import com.hodges.kevin.kotlin.enums.RestaurantPriceEnum
import com.hodges.kevin.kotlin.model.Restaurant

/**
 * Created by Kevin on 2/7/2018.
 */
interface RestaurantViewModel {

   fun addNewRestaurant(context: Context, restaurant: Restaurant): Boolean

   fun removeRestaurant(context: Context, restaurant: Restaurant?)

   fun removeAllRestaurants(context: Context)

   fun hasAnyRestaurantsAtThisPrice(desiredPriceMap: Map<RestaurantPriceEnum, Boolean>): Boolean

   fun getRandomRestaurant(desiredPrice: List<RestaurantPriceEnum>): Restaurant?
}
