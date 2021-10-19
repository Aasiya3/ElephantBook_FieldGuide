package com.example.elephantbook_fieldguide

import androidx.room.Entity
import org.json.JSONObject
import java.time.OffsetDateTime

@Entity(primaryKeys = ["dateTime", "elephantId"])
data class Location(
    val dateTime: OffsetDateTime,
    val latitude: Double,
    val longitude: Double,
    val elephantId: Int,
) {

    companion object{
        val converter = Converters()
    }

    constructor(id: Int, obj: JSONObject) : this(
        converter.toOffsetDateTime(obj.getString("datetime")),
        obj.getDouble("lat"),
        obj.getDouble("lon"),
        id
    )

    override fun toString(): String {
        return "$elephantId located $latitude, $longitude at $dateTime"
    }
}