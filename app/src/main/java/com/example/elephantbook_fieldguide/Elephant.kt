package com.example.elephantbook_fieldguide

import org.json.JSONObject
import java.time.OffsetDateTime
import java.util.*

class Elephant(
    val id: Int,
    val seek: String,
    val name: String,
) {
    constructor(obj : JSONObject) : this(obj.getInt("id"), obj.getString("seek"), obj.getString("name"))
}