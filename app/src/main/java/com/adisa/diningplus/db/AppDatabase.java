package com.adisa.diningplus.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.adisa.diningplus.db.dao.CourseDao;
import com.adisa.diningplus.db.dao.ItemDao;
import com.adisa.diningplus.db.dao.LocationDao;
import com.adisa.diningplus.db.dao.ManagerDao;
import com.adisa.diningplus.db.dao.MealDao;
import com.adisa.diningplus.db.dao.NutritionDao;
import com.adisa.diningplus.db.entities.Course;
import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Location;
import com.adisa.diningplus.db.entities.Manager;
import com.adisa.diningplus.db.entities.Meal;
import com.adisa.diningplus.db.entities.Nutrition;

@Database(entities = {
    Location.class,
    Manager.class,
    Meal.class,
    Course.class,
    Item.class,
    Nutrition.class,
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract ManagerDao managerDao();
    public abstract MealDao mealDao();
    public abstract CourseDao courseDao();
    public abstract ItemDao itemDao();
    public abstract NutritionDao nutritionDao();
}