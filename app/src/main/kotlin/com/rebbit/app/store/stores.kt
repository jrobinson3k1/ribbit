package com.rebbit.app.store

import android.content.SharedPreferences
import java.util.*

private const val KEY_UUID = "uuid"
private const val KEY_ACCESS_TOKEN = "access_token"
private const val KEY_EXPIRES_IN = "expires_in"
private const val KEY_EXPIRE_TIME = "expire_time"

class TokenStore(private val sharedPrefs: SharedPreferences) {

    var uuid: String
        private set(value) = sharedPrefs.saveString(KEY_UUID, value)
        get() = sharedPrefs.getString(KEY_UUID) ?: UUID.randomUUID().toString().also { uuid = it }

    var accessToken: String?
        private set(value) = sharedPrefs.saveString(KEY_ACCESS_TOKEN, value)
        get() = sharedPrefs.getString(KEY_ACCESS_TOKEN)

    var expiresIn: Long
        private set(value) = sharedPrefs.saveLong(KEY_EXPIRES_IN, value)
        get() = sharedPrefs.getLong(KEY_EXPIRES_IN)

    var expiresTime: Long
        private set(value) = sharedPrefs.saveLong(KEY_EXPIRE_TIME, value)
        get() = sharedPrefs.getLong(KEY_EXPIRE_TIME)

    fun putAccessToken(accessToken: String, expiresIn: Long) {
        this.accessToken = accessToken
        this.expiresIn = expiresIn
        this.expiresTime = System.currentTimeMillis() + expiresIn
    }

    fun isExpired() = System.currentTimeMillis() >= expiresTime
}

class UserStore(private val sharedPrefs: SharedPreferences) {

}

private fun SharedPreferences.saveString(key: String, value: String?) = edit().putString(key, value).apply()

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
private fun SharedPreferences.getString(key: String, defValue: String? = null): String? = getString(key, defValue)

private fun SharedPreferences.saveLong(key: String, value: Long) = edit().putLong(key, value).apply()

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
private fun SharedPreferences.getLong(key: String, defValue: Long = 0): Long = getLong(key, defValue)