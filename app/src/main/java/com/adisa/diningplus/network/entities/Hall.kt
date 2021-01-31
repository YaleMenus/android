package com.adisa.diningplus.network.entities

import android.location.Location
import org.json.JSONException
import org.json.JSONObject

class Hall(raw: JSONObject) {
    var id: String = raw.getString("id")
    var name: String = raw.getString("name")
    var nickname: String = raw.getString("nickname")
    var open: Boolean = raw.optBoolean("open")
    var occupancy: Int = raw.optInt("occupancy")
    var latitude: Double = raw.optDouble("latitude")
    var longitude: Double = raw.optDouble("longitude")
    var address: String = raw.optString("address")
    var phone: String = raw.optString("phone")

    var distance: Double = 0.0

    fun setDistance(location: Location?) {
        if (location != null) {
            val loc = Location("")
            loc.latitude = latitude
            loc.longitude = longitude
            // Convert m -> km
            distance = location.distanceTo(loc) / 1000.toDouble()
        }
    }
}