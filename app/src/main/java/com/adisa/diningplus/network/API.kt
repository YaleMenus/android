package com.adisa.diningplus.network

import android.content.Context
import com.adisa.diningplus.db.AppDatabase
import com.adisa.diningplus.db.DatabaseClient
import com.adisa.diningplus.db.entities.Item
import com.adisa.diningplus.db.entities.Hall
import com.adisa.diningplus.db.entities.Meal
import com.adisa.diningplus.db.entities.Nutrition
import com.adisa.diningplus.utils.DateFormatProvider
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.jvm.Throws

class API(ctx: Context?) {
    private val API_ROOT = "https://yaledine.com/api/"
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

    fun getHalls(): List<Hall> {
        val hallsRaw = JSONArray(getJSON("halls"))
        db.hallDao().clear()
        for (i in 0 until hallsRaw.length()) {
            val hallRaw = hallsRaw.getJSONObject(i)
            val hall = Hall.fromJSON(hallRaw)
            db.hallDao().insert(hall)
        }
        return db.hallDao().all
    }

    fun getHallMeals(hallId: String, date: Calendar): List<Meal> {
        val query = "date=" + DateFormatProvider.date.format(date.time)
        val mealsRaw = JSONArray(getJSON("halls/$hallId/meals?$query"))
        db.mealDao().clearHall(hallId)
        for (i in 0 until mealsRaw.length()) {
            val mealRaw = mealsRaw.getJSONObject(i)
            val meal = Meal.fromJSON(mealRaw)
            db.mealDao().insert(meal)
        }
        return db.mealDao().getHall(hallId)
    }

    fun getMealItems(mealId: Int): List<Item> {
        val items = db.itemDao().getMeal(mealId)
        if (!items.isEmpty()) {
            return items
        }
        val fetchedItems = ArrayList<Item>()
        val itemsRaw = JSONArray(getJSON("meals/$mealId/items"))
        for (i in 0 until itemsRaw.length()) {
            val itemRaw = itemsRaw.getJSONObject(i)
            val item = Item.fromJSON(itemRaw)
            fetchedItems.add(item)
            // db.itemDao().insert(item);
        }
        return fetchedItems
        // return db.itemDao().getMeal(mealId);
    }

    fun getItem(itemId: Int): Item {
        var item = db.itemDao()[itemId]
        if (item == null) {
            val itemRaw = JSONObject(getJSON("items/$itemId"))
            item = Item.fromJSON(itemRaw)
            db.itemDao().insert(item)
        }
        return item
    }

    fun getItemNutrition(itemId: Int): Nutrition {
        var nutrition = db.nutritionDao()[itemId]
        if (nutrition == null) {
            val nutritionRaw = JSONObject(getJSON("items/$itemId/nutrition"))
            nutrition = Nutrition.fromJSON(nutritionRaw);
        }
        return nutrition
    }

    init {
        val dbClient = DatabaseClient(ctx)
        db = dbClient.db
    }
}