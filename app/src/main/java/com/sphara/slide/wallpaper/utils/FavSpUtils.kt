package com.sphara.slide.wallpaper.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.json.JSONArray
import java.lang.Exception

object FavSpUtils {

    private const val DEFAULT_SP_NAME = "sp_string_utils"
    private var sharedPreferences: SharedPreferences? = null

    /**
     * 初始化（必须在 Application 中调用）
     */
    fun init(context: Context, spName: String = DEFAULT_SP_NAME) {
        sharedPreferences =
            context.applicationContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    /**
     * 存储字符串列表
     */
    fun putList(key: String, list: List<String>) {
        try {
            val jsonArray = JSONArray(list)
            val jsonString = jsonArray.toString()
            sharedPreferences?.edit { putString(key, jsonString) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取字符串列表
     * @param android.R.attr.defaultValue 默认值
     */
    fun getList(key: String): List<String> {
        val json = sharedPreferences?.getString(key, "") ?: ""

        if (json.isEmpty()) {
            return emptyList()
        }

        val jsonArray = JSONArray(json)
        val result = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            result.add(jsonArray.getString(i))
        }
        return result
    }
}