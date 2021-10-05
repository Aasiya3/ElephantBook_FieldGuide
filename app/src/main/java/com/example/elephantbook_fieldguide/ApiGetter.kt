package com.example.elephantbook_fieldguide

import java.util.*

class ApiGetter {
    val apiUrl = "https://example.com"

    fun updateElephantData(): List<Elephant> {
        val newElephants = mutableListOf<Elephant>()

        /*
        DO THE ACTUAL API INTERACTION, MOCKED HERE
         */

        /* MOCK */
        val myElephant = Elephant(
            1,
            "B70T01E808_-403_X00S00_",
            "FakeElephant",
            Date(1633467892034)
        )
        newElephants.add(myElephant)
        /* MOCK */

        return newElephants
    }
}