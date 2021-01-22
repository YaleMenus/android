package com.adisa.diningplus.network

import android.content.Context
import com.adisa.diningplus.db.AppDatabase
import com.adisa.diningplus.db.DatabaseClient
import com.adisa.diningplus.db.entities.Item
import com.adisa.diningplus.db.entities.Location
import com.adisa.diningplus.db.entities.Meal
import com.adisa.diningplus.db.entities.Nutrition
import com.adisa.diningplus.utils.DateFormatProvider
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.jvm.Throws

class API(ctx: Context?) {
    private val API_ROOT = "https://yaledine.herokuapp.com/api/"
    var db: AppDatabase

    @Throws(IOException::class)
    fun getJSON(endpoint: String): String {
        val url = URL(API_ROOT + endpoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        // Read data from connection
        val inputStream = connection.inputStream
        val buffer = StringBuilder()
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            buffer.append(line).append("\n")
        }
        //Log.d("API", buffer.toString());
        return buffer.toString()
    }

    @get:Throws(IOException::class, JSONException::class)
    val locations: List<Location>
        get() {
            val locationsRaw = JSONArray(getJSON("locations"))
            db.locationDao().clear()
            for (i in 0 until locationsRaw.length()) {
                val locationRaw = locationsRaw.getJSONObject(i)
                val location = Location()
                location.id = locationRaw.getInt("id")
                location.name = locationRaw.getString("name")
                location.shortname = locationRaw.getString("shortname")
                location.code = locationRaw.getString("code")
                location.open = locationRaw.optBoolean("open", false)
                location.occupancy = locationRaw.optInt("occupancy", 0)
                location.latitude = locationRaw.optDouble("latitude", 0.0)
                location.longitude = locationRaw.optDouble("longitude", 0.0)
                location.address = locationRaw.optString("address", null)
                location.phone = locationRaw.optString("phone", null)
                db.locationDao().insert(location)
            }
            return db.locationDao().all
        }

    @Throws(IOException::class, JSONException::class)
    fun getLocationMeals(locationId: Int, date: Calendar): List<Meal> {
        val query = "date=" + DateFormatProvider.date.format(date.time)
        val mealsRaw = JSONArray(getJSON("locations/$locationId/meals?$query"))
        db.mealDao().clearLocation(locationId)
        for (i in 0 until mealsRaw.length()) {
            val mealRaw = mealsRaw.getJSONObject(i)
            val meal = Meal()
            meal.id = mealRaw.getInt("id")
            meal.name = mealRaw.getString("name")
            meal.date = mealRaw.getString("date")
            meal.startTime = mealRaw.optString("start_time")
            meal.endTime = mealRaw.optString("end_time")
            meal.locationId = mealRaw.getInt("location_id")
            db.mealDao().insert(meal)
        }
        return db.mealDao().getLocation(locationId)
    }

    @Throws(IOException::class, JSONException::class)
    fun getMealItems(mealId: Int): List<Item> {
        val items = db.itemDao().getMeal(mealId)
        if (!items.isEmpty()) {
            return items
        }
        val fetchedItems = ArrayList<Item>()
        val itemsRaw = JSONArray(getJSON("meals/$mealId/items"))
        for (i in 0 until itemsRaw.length()) {
            val itemRaw = itemsRaw.getJSONObject(i)
            val item = Item()
            item.id = itemRaw.getInt("id")
            item.name = itemRaw.getString("name")
            item.course = itemRaw.getString("course")
            item.ingredients = itemRaw.getString("ingredients")
            item.meat = itemRaw.getBoolean("meat")
            item.animal_products = itemRaw.getBoolean("animal_products")
            item.alcohol = itemRaw.getBoolean("alcohol")
            item.nuts = itemRaw.getBoolean("nuts")
            item.shellfish = itemRaw.getBoolean("shellfish")
            item.peanuts = itemRaw.getBoolean("peanuts")
            item.dairy = itemRaw.getBoolean("dairy")
            item.egg = itemRaw.getBoolean("egg")
            item.pork = itemRaw.getBoolean("pork")
            item.fish = itemRaw.getBoolean("fish")
            item.soy = itemRaw.getBoolean("soy")
            item.wheat = itemRaw.getBoolean("wheat")
            item.gluten = itemRaw.getBoolean("gluten")
            item.coconut = itemRaw.getBoolean("coconut")
            fetchedItems.add(item)
            // db.itemDao().insert(item);
        }
        return fetchedItems
        // return db.itemDao().getMeal(mealId);
    }

    @Throws(IOException::class, JSONException::class)
    fun getItem(itemId: Int): Item {
        var item = db.itemDao()[itemId]
        if (item == null) {
            val itemRaw = JSONObject(getJSON("items/$itemId"))
            item = Item()
            item.id = itemRaw.getInt("id")
            item.name = itemRaw.getString("name")
            item.ingredients = itemRaw.getString("ingredients")
            item.meat = itemRaw.getBoolean("meat")
            item.animal_products = itemRaw.getBoolean("animal_products")
            item.alcohol = itemRaw.getBoolean("alcohol")
            item.nuts = itemRaw.getBoolean("nuts")
            item.shellfish = itemRaw.getBoolean("shellfish")
            item.peanuts = itemRaw.getBoolean("peanuts")
            item.dairy = itemRaw.getBoolean("dairy")
            item.egg = itemRaw.getBoolean("egg")
            item.pork = itemRaw.getBoolean("pork")
            item.fish = itemRaw.getBoolean("fish")
            item.soy = itemRaw.getBoolean("soy")
            item.wheat = itemRaw.getBoolean("wheat")
            item.gluten = itemRaw.getBoolean("gluten")
            item.coconut = itemRaw.getBoolean("coconut")
            db.itemDao().insert(item)
        }
        return item
    }

    @Throws(IOException::class, JSONException::class)
    fun getItemNutrition(itemId: Int): Nutrition {
        var nutrition = db.nutritionDao()[itemId]
        if (nutrition == null) {
            val nutritionRaw = JSONObject(getJSON("items/$itemId/nutrition"))
            nutrition = Nutrition()
            nutrition.portionSize = nutritionRaw.getString("portion_size")
            nutrition.calories = nutritionRaw.getString("calories")
            nutrition.totalFat = nutritionRaw.optString("total_fat")
            nutrition.saturatedFat = nutritionRaw.optString("saturated_fat")
            nutrition.transFat = nutritionRaw.optString("trans_fat")
            nutrition.cholesterol = nutritionRaw.optString("cholesterol")
            nutrition.sodium = nutritionRaw.optString("sodium")
            nutrition.totalCarbohydrate = nutritionRaw.optString("total_carbohydrate")
            nutrition.dietaryFiber = nutritionRaw.optString("dietary_fiber")
            nutrition.totalSugars = nutritionRaw.optString("total_sugars")
            nutrition.protein = nutritionRaw.optString("protein")
            nutrition.vitaminD = nutritionRaw.optString("vitamin_d")
            nutrition.vitaminA = nutritionRaw.optString("vitamin_a")
            nutrition.vitaminC = nutritionRaw.optString("vitamin_c")
            nutrition.calcium = nutritionRaw.optString("calcium")
            nutrition.iron = nutritionRaw.optString("iron")
            nutrition.potassium = nutritionRaw.optString("potassium")
            nutrition.totalFatPDV = nutritionRaw.optInt("total_fat_pdv")
            nutrition.saturatedFatPDV = nutritionRaw.optInt("saturated_fat_pdv")
            nutrition.transFatPDV = nutritionRaw.optInt("trans_fat_pdv")
            nutrition.cholesterolPDV = nutritionRaw.optInt("cholesterol_pdv")
            nutrition.sodiumPDV = nutritionRaw.optInt("sodium_pdv")
            nutrition.totalCarbohydratePDV = nutritionRaw.optInt("total_carbohydrate_pdv")
            nutrition.dietaryFiberPDV = nutritionRaw.optInt("dietary_fiber_pdv")
            nutrition.totalSugarsPDV = nutritionRaw.optInt("total_sugars_pdv")
            nutrition.proteinPDV = nutritionRaw.optInt("protein_pdv")
            nutrition.vitaminDPDV = nutritionRaw.optInt("vitamin_d_pdv")
            nutrition.vitaminAPDV = nutritionRaw.optInt("vitamin_a_pdv")
            nutrition.vitaminCPDV = nutritionRaw.optInt("vitamin_c_pdv")
            nutrition.calciumPDV = nutritionRaw.optInt("calcium_pdv")
            nutrition.ironPDV = nutritionRaw.optInt("iron_pdv")
            nutrition.potassiumPDV = nutritionRaw.optInt("potassium_pdv")
            nutrition.itemId = nutritionRaw.getInt("item_id")
        }
        return nutrition
    }

    init {
        val dbClient = DatabaseClient(ctx)
        db = dbClient.db
    }
}