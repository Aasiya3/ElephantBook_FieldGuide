package com.example.elephantbook_fieldguide

import android.util.Base64

class Secrets {
    companion object {
        const val apiUrl = "https://localhost/individuals.json"
        const val imageUrl = "https://localhost/"
        private const val apiUsername = "user"
        private const val apiPassword = "pass"

        val apiAuthHeaders = mapOf("Authorization" to "Basic ${
            // https://en.wikipedia.org/wiki/Basic_access_authentication
            Base64.encodeToString(
                    "${apiUsername}:${apiPassword}".toByteArray(),
                    Base64.NO_WRAP
            )
        }")
    }
}