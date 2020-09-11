package com.adisa.diningplus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.adisa.diningplus.BuildConfig;
import com.adisa.diningplus.utils.DateFormatProvider;

import java.util.Date;

/**
 * Created by Adisa on 3/20/2017.
 */

public final class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "dining.db";
    private static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + DatabaseContract.Location.TABLE_NAME + " (" +
                    DatabaseContract.Location.ID + " INT PRIMARY KEY," +
                    DatabaseContract.Location.NAME + " TEXT," +
                    DatabaseContract.Location.TYPE + " TEXT," +
                    DatabaseContract.Location.IS_OPEN + " BOOL," +
                    DatabaseContract.Location.CAPACITY + " INT," +
                    DatabaseContract.Location.LATITUDE + " FLOAT," +
                    DatabaseContract.Location.LONGITUDE + " FLOAT," +
                    DatabaseContract.Location.ADDRESS + " TEXT," +
                    DatabaseContract.Location.PHONE + " TEXT," +
                    DatabaseContract.Location.LAST_UPDATED + " TEXT" +
            ");";

    private static final String SQL_CREATE_MANAGERS =
            "CREATE TABLE " + DatabaseContract.Manager.TABLE_NAME + " (" +
                    DatabaseContract.Manager.NAME + " TEXT," +
                    DatabaseContract.Manager.EMAIL + " TEXT" +
            ");";

    private static final String SQL_CREATE_MEALS =
            "CREATE TABLE " + DatabaseContract.Meal.TABLE_NAME + " (" +
                    DatabaseContract.Meal.ID + " INT PRIMARY KEY," +
                    DatabaseContract.Meal.NAME + " TEXT," +
                    DatabaseContract.Meal.DATE + " TEXT," +
                    DatabaseContract.Meal.LOCATION_ID + " INT," +

                    "FOREIGN KEY (" + DatabaseContract.Meal.LOCATION_ID +
                        ") REFERENCES " + DatabaseContract.Location.TABLE_NAME + "(" + DatabaseContract.Location.ID + ")" +
            ");";

    public static final String SQL_CREATE_COURSES =
            "CREATE TABLE " + DatabaseContract.Course.TABLE_NAME + " (" +
                    DatabaseContract.Course.ID + " INT PRIMARY KEY," +
                    DatabaseContract.Course.NAME + " TEXT," +
                    "FOREIGN KEY (" + DatabaseContract.Course.MEAL_ID +
                        ") REFERENCES " + DatabaseContract.Meal.TABLE_NAME + "(" + DatabaseContract.Meal.ID + ")" +
            ");";

    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + DatabaseContract.Item.TABLE_NAME + " (" +
                    DatabaseContract.Item.ID + " INT PRIMARY KEY," +
                    DatabaseContract.Item.NAME + " TEXT," +
                    DatabaseContract.Item.INGREDIENTS + " TEXT," +
                    DatabaseContract.Item.VEGETARIAN + " BOOL," +
                    DatabaseContract.Item.VEGAN + " BOOL," +
                    DatabaseContract.Item.ALCOHOL + " BOOL," +
                    DatabaseContract.Item.NUTS + " BOOL," +
                    DatabaseContract.Item.SHELLFISH + " BOOL," +
                    DatabaseContract.Item.PEANUTS + " BOOL," +
                    DatabaseContract.Item.DAIRY + " BOOL," +
                    DatabaseContract.Item.EGG + " BOOL," +
                    DatabaseContract.Item.PORK + " BOOL," +
                    DatabaseContract.Item.SEAFOOD + " BOOL," +
                    DatabaseContract.Item.SOY + " BOOL," +
                    DatabaseContract.Item.WHEAT + " BOOL," +
                    DatabaseContract.Item.GLUTEN + " BOOL," +
                    DatabaseContract.Item.COCONUT + " BOOL," +
                    "FOREIGN KEY (" + DatabaseContract.Item.MEAL_ID +
                        ") REFERENCES " + DatabaseContract.Meal.TABLE_NAME + "(" + DatabaseContract.Meal.ID + ")," +
                    "FOREIGN KEY (" + DatabaseContract.Item.COURSE_ID +
                        ") REFERENCES " + DatabaseContract.Course.TABLE_NAME + "(" + DatabaseContract.Course.ID + ")" +
            ");";

    private static final String SQL_CREATE_NUTRITION =
            "CREATE TABLE " + DatabaseContract.Nutrition.TABLE_NAME + " (" +
                    DatabaseContract.Nutrition.ID + " INT PRIMARY KEY," +
                    DatabaseContract.Nutrition.NAME + " TEXT," +
                    DatabaseContract.Nutrition.PORTION_SIZE + " TEXT," +
                    DatabaseContract.Nutrition.CALORIES + " TEXT," +

                    DatabaseContract.Nutrition.TOTAL_FAT + " TEXT," +
                    DatabaseContract.Nutrition.SATURATED_FAT + " TEXT," +
                    DatabaseContract.Nutrition.TRANS_FAT + " TEXT," +
                    DatabaseContract.Nutrition.CHOLESTEROL + " TEXT," +
                    DatabaseContract.Nutrition.SODIUM + " TEXT," +
                    DatabaseContract.Nutrition.TOTAL_CARBOHYDRATE + " TEXT," +
                    DatabaseContract.Nutrition.DIETARY_FIBER + " TEXT," +
                    DatabaseContract.Nutrition.TOTAL_SUGARS + " TEXT," +
                    DatabaseContract.Nutrition.PROTEIN + " TEXT," +
                    DatabaseContract.Nutrition.VITAMIN_D + " TEXT," +
                    DatabaseContract.Nutrition.VITAMIN_A + " TEXT," +
                    DatabaseContract.Nutrition.VITAMIN_C + " TEXT," +
                    DatabaseContract.Nutrition.CALCIUM + " TEXT," +
                    DatabaseContract.Nutrition.IRON + " TEXT," +
                    DatabaseContract.Nutrition.POTASSIUM + " TEXT," +

                    DatabaseContract.Nutrition.TOTAL_FAT_PDV + " INT," +
                    DatabaseContract.Nutrition.SATURATED_FAT_PDV + " INT," +
                    DatabaseContract.Nutrition.TRANS_FAT_PDV + " INT," +
                    DatabaseContract.Nutrition.CHOLESTEROL_PDV + " INT," +
                    DatabaseContract.Nutrition.SODIUM_PDV + " INT," +
                    DatabaseContract.Nutrition.TOTAL_CARBOHYDRATE_PDV + " INT," +
                    DatabaseContract.Nutrition.DIETARY_FIBER_PDV + " INT," +
                    DatabaseContract.Nutrition.TOTAL_SUGARS_PDV + " INT," +
                    DatabaseContract.Nutrition.PROTEIN_PDV + " INT," +
                    DatabaseContract.Nutrition.VITAMIN_D_PDV + " INT," +
                    DatabaseContract.Nutrition.VITAMIN_A_PDV + " INT," +
                    DatabaseContract.Nutrition.VITAMIN_C_PDV + " INT," +
                    DatabaseContract.Nutrition.CALCIUM_PDV + " INT," +
                    DatabaseContract.Nutrition.IRON_PDV + " INT," +
                    DatabaseContract.Nutrition.POTASSIUM_PDV + " INT," +

                    "FOREIGN KEY (" + DatabaseContract.Nutrition.ITEM_ID +
                        ") REFERENCES " + DatabaseContract.Item.TABLE_NAME + "(" + DatabaseContract.Item.ID + ")" +
            ");";

    private static final String SQL_DELETE_LOCATIONS =
            "DROP TABLE IF EXISTS " + DatabaseContract.Location.TABLE_NAME;
    private static final String SQL_DELETE_MANAGERS =
            "DROP TABLE IF EXISTS " + DatabaseContract.Manager.TABLE_NAME;
    private static final String SQL_DELETE_MEALS =
            "DROP TABLE IF EXISTS " + DatabaseContract.Meal.TABLE_NAME;
    private static final String SQL_DELETE_COURSES =
            "DROP TABLE IF EXISTS " + DatabaseContract.Course.TABLE_NAME;
    private static final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS " + DatabaseContract.Item.TABLE_NAME;
    private static final String SQL_DELETE_NUTRITION =
            "DROP TABLE IF EXISTS " + DatabaseContract.Nutrition.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATIONS);
        db.execSQL(SQL_CREATE_MANAGERS);
        db.execSQL(SQL_CREATE_MEALS);
        db.execSQL(SQL_CREATE_COURSES);
        db.execSQL(SQL_CREATE_ITEMS);
        db.execSQL(SQL_CREATE_NUTRITION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_LOCATIONS);
        db.execSQL(SQL_DELETE_MANAGERS);
        db.execSQL(SQL_DELETE_MEALS);
        db.execSQL(SQL_DELETE_COURSES);
        db.execSQL(SQL_DELETE_ITEMS);
        db.execSQL(SQL_DELETE_NUTRITION);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    void resetLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_LOCATIONS);
        db.execSQL(SQL_CREATE_LOCATIONS);
    }

    void reset() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_LOCATIONS);
        db.execSQL(SQL_DELETE_MANAGERS);
        db.execSQL(SQL_DELETE_MEALS);
        db.execSQL(SQL_DELETE_COURSES);
        db.execSQL(SQL_DELETE_ITEMS);
        db.execSQL(SQL_DELETE_NUTRITION);
        onCreate(db);
    }

    public void insertLocation(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Location.TABLE_NAME, null, values);
    }

    public void updateLocation(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Location.ID)};
        int count = db.update(DatabaseContract.Location.TABLE_NAME, values, DatabaseContract.Location.ID + " = ?", args);
        if (BuildConfig.DEBUG && count != 1)
            throw new AssertionError();
    }

    public void insertManager(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Manager.TABLE_NAME, null, values);
    }

    public void updateManager(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Manager.ID)};
        int count = db.update(DatabaseContract.Manager.TABLE_NAME, values, DatabaseContract.Manager.ID + " = ?", args);
        if (BuildConfig.DEBUG && count != 1)
            throw new AssertionError();
    }

    public void insertMeal(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Meal.TABLE_NAME, null, values);
    }

    public void updateMeal(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Meal.ID)};
        int count = db.update(DatabaseContract.Meal.TABLE_NAME, values, DatabaseContract.Meal.ID + " = ?", args);
        if (BuildConfig.DEBUG && count != 1)
            throw new AssertionError();
    }

    public void insertCourse(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Course.TABLE_NAME, null, values);
    }

    public void updateCourse(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Course.ID)};
        int count = db.update(DatabaseContract.Manager.TABLE_NAME, values, DatabaseContract.Course.ID + " = ?", args);
        if (BuildConfig.DEBUG && count != 1)
            throw new AssertionError();
    }

    public void insertItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Item.TABLE_NAME, null, values);
    }

    public void updateItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Item.ID)};
        int count = db.update(DatabaseContract.Item.TABLE_NAME, values, DatabaseContract.Item.ID + " = ?", args);
        if (BuildConfig.DEBUG && count != 1)
            throw new AssertionError();
    }

    public void insertNutrition(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Nutrition.TABLE_NAME, null, values);
    }

    public void updateNutrition(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Nutrition.ID)};
        int count = db.update(DatabaseContract.Nutrition.TABLE_NAME, values, DatabaseContract.Nutrition.ID + " = ?", args);
        if (BuildConfig.DEBUG && count != 1)
            throw new AssertionError();
    }

    public boolean isStored(String tableName, String field, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName + " WHERE " + field + " = " + value;
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        return cursor.getCount() > 0;
    }

    Cursor getLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Location.TABLE_NAME;
        return db.rawQuery(query, null);
    }

    Cursor getLocation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Location.TABLE_NAME + " WHERE " + DatabaseContract.Location.ID + " = " + id;
        return db.rawQuery(query, null);
    }

    Cursor getLocationManagers(int locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Manager.TABLE_NAME + " WHERE " + DatabaseContract.Manager.LOCATION_ID + " = " + locationId;
        return db.rawQuery(query, null);
    }

    Cursor getManager(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Manager.TABLE_NAME + " WHERE " + DatabaseContract.Manager.ID + " = " + id;
        return db.rawQuery(query, null);
    }

    Cursor getMeals(int locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Meal.TABLE_NAME + " WHERE " + DatabaseContract.Meal.LOCATION_ID + " = " + locationId;
        return db.rawQuery(query, null);
    }

    Cursor getMeal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Meal.TABLE_NAME + " WHERE " + DatabaseContract.Meal.ID + " = " + id;
        return db.rawQuery(query, null);
    }

    Cursor getMealCourses(int mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Course.TABLE_NAME + " WHERE " + DatabaseContract.Course.MEAL_ID + " = " + mealId;
        return db.rawQuery(query, null);
    }

    Cursor getCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Course.TABLE_NAME + " WHERE " + DatabaseContract.Course.ID + " = " + id;
        return db.rawQuery(query, null);
    }

    Cursor getMealItems(int mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Item.TABLE_NAME + " WHERE " + DatabaseContract.Item.MEAL_ID + " = " + mealId;
        return db.rawQuery(query, null);
    }

    Cursor getCourseItems(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Item.TABLE_NAME + " WHERE " + DatabaseContract.Item.COURSE_ID + " = " + courseId;
        return db.rawQuery(query, null);
    }

    Cursor getItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Item.TABLE_NAME + " WHERE " + DatabaseContract.Item.ID + " = " + id;
        return db.rawQuery(query, null);
    }

    Cursor getItemNutrition(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseContract.Nutrition.TABLE_NAME + " WHERE " + DatabaseContract.Nutrition.ITEM_ID + " = " + itemId;
        return db.rawQuery(query, null);
    }
}