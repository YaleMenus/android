package com.adisa.diningplus.network.entities

import org.json.JSONObject

class Manager(raw: JSONObject) {
    var id: Int = raw.getInt("id")
    var name: String = raw.getString("name")
    var email: String? = raw.optString("email")
    var position: String? = raw.optString("position")
}