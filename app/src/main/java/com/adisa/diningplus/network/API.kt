package com.adisa.diningplus.network

import android.content.Context
import com.adisa.diningplus.network.entities.Item
import com.adisa.diningplus.network.entities.Hall
import com.adisa.diningplus.network.entities.Meal
import com.adisa.diningplus.network.entities.Nutrition
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
        return buffer.toString()
    }

    fun getHalls(): List<Hall> {
        val hallsRaw = JSONArray(getJSON("halls"))
        val halls = ArrayList<Hall>()
        for (i in 0 until hallsRaw.length()) {
            val hallRaw = hallsRaw.getJSONObject(i)
            halls.add(Hall.fromJSON(hallRaw))
        }
        return halls
    }

    fun getHallMeals(hallId: String, date: Calendar): List<Meal> {
        val query = "date=" + DateFormatProvider.date.format(date.time)
        val mealsRaw = JSONArray(getJSON("halls/$hallId/meals?$query"))
        val meals = ArrayList<Meal>()
        for (i in 0 until mealsRaw.length()) {
            val mealRaw = mealsRaw.getJSONObject(i)
            meals.add(Meal.fromJSON(mealRaw))
        }
        return meals
    }

    fun getMealItems(mealId: Int): List<Item> {
        val fetchedItems = ArrayList<Item>()
        val itemsRaw = JSONArray(getJSON("meals/$mealId/items"))
        val items = ArrayList<Item>()
        for (i in 0 until itemsRaw.length()) {
            val itemRaw = itemsRaw.getJSONObject(i)
            val item = Item.fromJSON(itemRaw)
            items.add(item)
        }
        return items
    }

    fun getItem(itemId: Int): Item {
        val itemRaw = JSONObject(getJSON("items/$itemId"))
        return Item.fromJSON(itemRaw)
    }

    fun getItemNutrition(itemId: Int): Nutrition {
        val nutritionRaw = JSONObject(getJSON("items/$itemId/nutrition"))
        return Nutrition.fromJSON(nutritionRaw)
    }
}