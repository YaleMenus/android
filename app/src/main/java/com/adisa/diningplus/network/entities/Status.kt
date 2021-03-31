package com.adisa.diningplus.network.entities

import org.json.JSONObject

class Status(raw: JSONObject) {
    var message: String = raw.optString("message")
    var minVersion: Int = raw.optInt("min_version_android")
}