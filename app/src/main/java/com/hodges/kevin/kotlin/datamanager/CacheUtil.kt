package com.hodges.kevin.kotlin.datamanager

import android.content.Context
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by Kevin on 2/2/2018.
 */
object CacheUtil {

    private val gson = Gson()

    fun <T>saveToStorage(context: Context, key: String, value: T ) {
        val prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)

        if (prefs != null) {
            val prefsEditor = prefs.edit()
            prefsEditor.putString(key, gson.toJson(value))
            prefsEditor.commit()
        }
    }

    fun <T>loadFromStorage(context: Context, key: String, type: Type): T {
        val prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val json = prefs.getString(key, "")
        return gson.fromJson(json, type)
    }
}
