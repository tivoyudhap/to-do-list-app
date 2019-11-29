package com.mastip.personaltodolist.utilities

import android.content.SharedPreferences

object CustomSharedPreferences {

    var sharedPreferences: SharedPreferences? = null

    fun getDataString(key: String?): String {
        sharedPreferences?.let { sharedPreferences ->
            sharedPreferences.getString(key, "")?.let { string ->
                return string
            }
        }

        return ""
    }

    fun getDataInt(key: String?): Int {
        sharedPreferences?.let { sharedPreferences ->
            sharedPreferences.getInt(key, 0)?.let { int ->
                return int
            }
        }

        return 0
    }

    fun putDataString(key: String?, value: String?) {
        sharedPreferences?.let { sharedPreferences ->
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun deleteDataString(key: String?) {
        sharedPreferences?.let { sharedPreferences ->
            val editor = sharedPreferences.edit()
            editor.remove(key)
            editor.apply()
        }
    }
}