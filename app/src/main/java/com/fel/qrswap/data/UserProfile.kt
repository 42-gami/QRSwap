package com.fel.qrswap.data

import android.content.Context

object UserProfile {

    private const val PREFS_NAME = "profile"
    private const val KEY_INITIALS = "initials"
    private const val KEY_COUNTRY_INDEX = "country_index"

    private const val DEFAULT_INITIALS = "???"
    private const val DEFAULT_COUNTRY_INDEX = 0  // Unknown / pirate flag

    var initials: String = DEFAULT_INITIALS
        private set

    var country: Country = Countries.list[DEFAULT_COUNTRY_INDEX]
        private set

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        initials = prefs.getString(KEY_INITIALS, DEFAULT_INITIALS) ?: DEFAULT_INITIALS
        country = Countries.fromIndex(prefs.getInt(KEY_COUNTRY_INDEX, DEFAULT_COUNTRY_INDEX))
    }

    fun save(context: Context, newInitials: String, newCountry: Country) {
        initials = newInitials.take(3)
        country = newCountry
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_INITIALS, initials)
            .putInt(KEY_COUNTRY_INDEX, Countries.toIndex(newCountry))
            .apply()
    }

    fun isSetUp(): Boolean = initials != DEFAULT_INITIALS
}