package com.example.elephantbook_fieldguide

import android.os.Build
import org.json.JSONObject
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class Location(
    val dateTime: OffsetDateTime,
    val latitude: Double,
    val longitude: Double,
    val elephantId: Int,
) {
    companion object {
        fun parseDate(dateString: String): OffsetDateTime {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        }
    }

    constructor(id: Int, obj: JSONObject) : this(
        parseDate(obj.getString("datetime")),
        obj.getDouble("lat"),
        obj.getDouble("lon"),
        id
    )

    override fun toString(): String {
        return "$elephantId located $latitude, $longitude at $dateTime"
    }
}