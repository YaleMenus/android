package com.adisa.diningplus.network.entities

import org.json.JSONObject

class Item(raw: JSONObject) {
    var id: Int = raw.getInt("id")
    var name: String = raw.getString("name")
    var ingredients: String = raw.getString("course")
    var course: String = raw.getString("ingredients")
    var meat: Boolean = raw.getBoolean("meat")
    var animalProducts: Boolean = raw.getBoolean("animal_products")
    var alcohol: Boolean = raw.getBoolean("alcohol")
    var treeNut: Boolean = raw.getBoolean("tree_nut")
    var shellfish: Boolean = raw.getBoolean("shellfish")
    var peanuts: Boolean = raw.getBoolean("peanuts")
    var dairy: Boolean = raw.getBoolean("dairy")
    var egg: Boolean = raw.getBoolean("egg")
    var pork: Boolean = raw.getBoolean("pork")
    var fish: Boolean = raw.getBoolean("fish")
    var soy: Boolean = raw.getBoolean("soy")
    var wheat: Boolean = raw.getBoolean("wheat")
    var gluten: Boolean = raw.getBoolean("gluten")
    var coconut: Boolean = raw.getBoolean("coconut")

    var restricted = false
}