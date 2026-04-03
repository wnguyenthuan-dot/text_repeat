package com.example.nt.app.textrepeat.utils.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import com.example.nt.app.textrepeat.model.TrendingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.isInitialized
import kotlin.text.trim

object SharePreferUtils {

    const val PER_NAME = "data_app_shared_preference"

    lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        if (!SharePreferUtils::sharePref.isInitialized) {
            sharePref = context.getSharedPreferences(PER_NAME, Context.MODE_PRIVATE)
        }
    }

    fun <T> saveKey(key: String, value: T) {
        when (value) {
            is String -> sharePref.edit().putString(key, value).apply()
            is Int -> sharePref.edit().putInt(key, value).apply()
            is Boolean -> sharePref.edit().putBoolean(key, value).apply()
            is Long -> sharePref.edit().putLong(key, value).apply()
            is Float -> sharePref.edit().putFloat(key, value).apply()
        }
    }

    fun getString(key: String, value: String = ""): String {
        return sharePref.getString(key, value)?.trim() ?: value
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharePref.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharePref.getBoolean(key, defaultValue)
    }

    fun getLong(key: String): Long {
        return sharePref.getLong(key, 0L)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharePref.getFloat(key, defaultValue)
    }

    //trending
    fun saveTrendingList(list: List<TrendingModel>) {
        val json = Gson().toJson(list)
        saveKey("saved_trending_list", json)
    }

    fun getTrendingList(): MutableList<TrendingModel>? {
        val json = getString("saved_trending_list", "")
        if (json.isEmpty()) return null

        val type = object : TypeToken<MutableList<TrendingModel>>() {}.type
        return try {
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }
    //======================================   APP   ===============================================

    fun getUnlock(value: Int): Long = getLong("getUnlock_$value")
    fun setUnlock(value: Int) = saveKey("getUnlock_$value", System.currentTimeMillis())

    fun isDownloaded(value: Int): Boolean = getBoolean("isDownloaded_$value")
    fun setDownloaded(value: Int) = saveKey("isDownloaded_$value", true)
}