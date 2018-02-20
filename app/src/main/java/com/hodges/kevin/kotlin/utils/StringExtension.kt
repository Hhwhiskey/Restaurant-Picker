package com.hodges.kevin.kotlin.utils

/**
 * Created by Kevin on 2/17/2018.
 */
class StringExtension {

    fun String.pluralize(count: Int): String? {
        return if (count > 1) {
            this + "s"
        }else {
            this
        }
    }

}