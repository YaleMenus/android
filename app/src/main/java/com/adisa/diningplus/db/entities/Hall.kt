package com.adisa.diningplus.db.entities

import android.location.Location
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "halls")
data class Hall(val raw: JSONObject) {
    @PrimaryKey
    var id: String = raw.getString("id")
    var name: String = raw.getString("name")
    var nickname: String = raw.getString("nickname")
    var open: Boolean = raw.getBoolean("open")
    var occupancy: Int = raw.getInt("occupancy")
    var latitude: Double = raw.getDouble("latitude")
    var longitude: Double = raw.getDouble("longitude")
    var address: String = raw.getString("address")
    var phone: String = raw.getString("phone")

    @Ignore
    var distance = 0.0
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