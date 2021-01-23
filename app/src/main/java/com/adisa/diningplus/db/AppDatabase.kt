package com.adisa.diningplus.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adisa.diningplus.db.dao.*
import com.adisa.diningplus.db.entities.*

@Database(entities = [
    Hall::class,
    Manager::class,
    Meal::class,
    Item::class,
    Nutrition::class
], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hallDao(): HallDao?
    abstract fun managerDao(): ManagerDao?
    abstract fun mealDao(): MealDao?
    abstract fun itemDao(): ItemDao?
    abstract fun nutritionDao(): NutritionDao?
}