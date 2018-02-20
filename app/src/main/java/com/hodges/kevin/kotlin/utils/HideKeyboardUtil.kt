package com.hodges.kevin.kotlin.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Kevin on 2/7/2018.
 */
object HideKeyboardUtil {

    fun hideKeyboard(context: Context, view: View) {

        val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromInputMethod(view.windowToken, 0)
    }
}