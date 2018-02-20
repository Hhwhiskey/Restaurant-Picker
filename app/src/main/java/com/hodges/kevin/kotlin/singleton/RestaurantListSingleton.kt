package com.hodges.kevin.kotlin.singleton

import com.google.gson.reflect.TypeToken
import com.hodges.kevin.kotlin.model.Restaurant
import java.lang.reflect.Type

/**
 * Created by Kevin on 2/7/2018.
 */
object RestaurantListSingleton  {

    var list: MutableList<Restaurant> = mutableListOf()
    val RESTAURANT_LIST_KEY = "restaurantListKey"
    val type: Type = object : TypeToken<List<Restaurant>>() {}.type
}