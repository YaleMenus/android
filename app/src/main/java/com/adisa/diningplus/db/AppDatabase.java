package com.adisa.diningplus.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.adisa.diningplus.db.dao.ItemDao;
import com.adisa.diningplus.db.dao.HallDao;
import com.adisa.diningplus.db.dao.ManagerDao;
import com.adisa.diningplus.db.dao.MealDao;
import com.adisa.diningplus.db.dao.NutritionDao;
import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Hall;
import com.adisa.diningplus.db.entities.Manager;
import com.adisa.diningplus.db.entities.Meal;
import com.adisa.diningplus.db.entities.Nutrition;

@Database(entities = {
    Hall.class,
    Manager.class,
    Meal.class,
    Item.class,
    Nutrition.class,
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HallDao hallDao();
    public abstract ManagerDao managerDao();
    public abstract MealDao mealDao();
    public abstract ItemDao itemDao();
    public abstract NutritionDao nutritionDao();
}