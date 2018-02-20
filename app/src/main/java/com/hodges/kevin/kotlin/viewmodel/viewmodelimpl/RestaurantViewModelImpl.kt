package com.hodges.kevin.kotlin.viewmodel.viewmodelimpl

import android.content.Context
import com.hodges.kevin.kotlin.datamanager.CacheUtil
import com.hodges.kevin.kotlin.enums.RestaurantPriceEnum
import com.hodges.kevin.kotlin.model.Restaurant
import com.hodges.kevin.kotlin.singleton.RestaurantListSingleton
import com.hodges.kevin.kotlin.singleton.RestaurantListSingleton.RESTAURANT_LIST_KEY
import com.hodges.kevin.kotlin.viewmodel.RestaurantViewModel
import java.util.*

/**
 * Created by Kevin on 2/7/2018.
 */
class RestaurantViewModelImpl : RestaurantViewModel {

    override fun addNewRestaurant(context: Context, restaurant: Restaurant): Boolean {
        if (!RestaurantListSingleton.list.contains(restaurant)) {
            RestaurantListSingleton.list.add(restaurant)
            CacheUtil.saveToStorage(context, RESTAURANT_LIST_KEY, RestaurantListSingleton.list)
            return true
        }
        return false
    }

    override fun removeRestaurant(context: Context, restaurant: Restaurant?) {
        RestaurantListSingleton.list.remove(restaurant)
        CacheUtil.saveToStorage(context, RESTAURANT_LIST_KEY, RestaurantListSingleton.list)
    }

    override fun removeAllRestaurants(context: Context) {
        RestaurantListSingleton.list.clear()
        CacheUtil.saveToStorage(context, RESTAURANT_LIST_KEY, RestaurantListSingleton.list)
    }


    /**
     * Every time a box is clicked, check to see if the user has any restaurants at the price of the checked boxes
     */
    override fun hasAnyRestaurantsAtThisPrice(desiredPriceMap: Map<RestaurantPriceEnum, Boolean>): Boolean {

//        // For each item in the map, check if the value is true, if so, filter by that key(CHEAP, AVERAGE, PRICEY)
//        for (item in desiredPriceMap) {
//            if (item.value) {
//                item.key
//            }
//        }
//
//        val filteredList = RestaurantListSingleton.list.filter {  }
//
//        if (filteredList.isNotEmpty()) {
//            return true
//        }
        return false
    }

    override fun getRandomRestaurant(desiredPrice: List<RestaurantPriceEnum>): Restaurant? {
        val filteredList = RestaurantListSingleton.list.filter { desiredPrice.contains(it.priceCategory) }
        if (filteredList.isNotEmpty()) {
            return filteredList[Random().nextInt(filteredList.size)]
        }
        return null
    }
}