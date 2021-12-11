package com.example.elephantbook_fieldguide

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Base64

class Secrets {
    companion object {
        var apiUrl = "https://localhost/individuals.json"
        var imageUrl = "https://localhost/"
        var apiUsername = "user"
        var apiPassword = "pass"

        fun getApiAuthHeaders() = mapOf(
            "Authorization" to "Basic ${
                // https://en.wikipedia.org/wiki/Basic_access_authentication
                Base64.encodeToString(
                    "${apiUsername}:${apiPassword}".toByteArray(),
                    Base64.NO_WRAP
                )
            }"
        )

        private fun getPreference(ctx: Context, key: String, default: String): String =
            ctx.getSharedPreferences(
                ctx.getString(R.string.preference_secrets),
                MODE_PRIVATE
            ).getString(key, null) ?: default

        fun initializeSecrets(ctx: Context) {
            apiUrl = getPreference(ctx, "apiUrl", apiUrl)
            imageUrl = getPreference(ctx, "imageUrl", imageUrl)
            apiUsername = getPreference(ctx, "apiUsername", apiUsername)
            apiPassword = getPreference(ctx, "apiPassword", apiPassword)
        }


        fun updateSecrets(
            apiUrl: String,
            imageUrl: String,
            apiUsername: String,
            apiPassword: String,
            ctx: Context
        ) {
            ctx.getSharedPreferences(
                ctx.getString(R.string.preference_secrets),
                MODE_PRIVATE
            ).edit().apply {
                putString("apiUrl", apiUrl)
                putString("imageUrl", imageUrl)
                putString("apiUsername", apiUsername)
                putString("apiPassword", apiPassword)
                apply()
            }
            initializeSecrets(ctx)
        }
    }
}