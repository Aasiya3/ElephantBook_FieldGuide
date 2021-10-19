package com.example.elephantbook_fieldguide

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity
data class Elephant(
    @PrimaryKey val id: Int,
    val seek: String,
    val name: String,
    val pfp: String
) {
    constructor(obj: JSONObject) : this(
        obj.getInt("id"),
        obj.getString("seek"),
        obj.getString("name"),
        obj.getString("pfp"),
    )

    override fun toString(): String {
        return "$name($id): $seek"
    }
}