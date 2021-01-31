package com.adisa.diningplus.network.entities

import org.json.JSONObject

class Meal(raw: JSONObject) {
    var id = raw.getInt("id")
    var name: String = raw.getString("name")
    var date: String = raw.getString("date")
    var startTime: String = raw.optString("start_time")
    var endTime: String = raw.optString("end_time")
    var hallId: String = raw.getString("hall_id")
}