package com.adisa.diningplus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.adisa.diningplus.BuildConfig;
import com.adisa.diningplus.utils.DateFormatProvider;

import java.util.Date;

/**
 * Created by Adisa on 3/20/2017.
 */

public final class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Dining.db";
    private static final String SQL_DININGHALL_CREATE =
            "create table " + DiningContract.Location.TABLE_NAME + " (" +
                    DiningContract.Location._ID + " int primary key," +
                    DiningContract.Location.NAME + " text," +
                    DiningContract.Location.ADDRESS + " text," +
                    DiningContract.Location.CAPACITY + " int," +
                    DiningContract.Location.IS_CLOSED + " boolean," +
                    DiningContract.Location.LATITUDE + " text," +
                    DiningContract.Location.LONGITUDE + " text," +
                    DiningContract.Location.MANAGER1_EMAIL + " text," +
                    DiningContract.Location.MANAGER1_NAME + " text," +
                    DiningContract.Location.MANAGER2_EMAIL + " text," +
                    DiningContract.Location.MANAGER2_NAME + " text," +
                    DiningContract.Location.MANAGER3_EMAIL + " text," +
                    DiningContract.Location.MANAGER3_NAME + " text," +
                    DiningContract.Location.MANAGER4_EMAIL + " text," +
                    DiningContract.Location.MANAGER4_NAME + " text," +
                    DiningContract.Location.PHONE + " text," +
                    DiningContract.Location.TYPE + " text," +
                    DiningContract.Location.LAST_UPDATED + " text" +
                    ");";

    private static final String SQL_MENUITEM_CREATE =
            "create table " + DiningContract.Item.TABLE_NAME + " (" +
                    DiningContract.Item._ID + " int primary key," +
                    DiningContract.Item.NAME + " text," +
                    DiningContract.Item.DINING_HALL + " int," +
                    DiningContract.Item.MENU_NAME + " text," +
                    DiningContract.Item.MENU_CODE + " int," +
                    DiningContract.Item.DATE + " text," +
                    DiningContract.Item.START_TIME + " text," +
                    DiningContract.Item.END_TIME + " text," +
                    DiningContract.Item.NUTRITION_ID + " text," +
                    "foreign key (" + DiningContract.Item.DINING_HALL + ") references " + DiningContract.Location.TABLE_NAME + "("
                    + DiningContract.Location._ID + ")," +
                    "foreign key (" + DiningContract.Item.NUTRITION_ID + ") references " + DiningContract.Nutrition.TABLE_NAME +
                    "(" + DiningContract.Nutrition._ID + ")" +
                    ");";

    private static final String SQL_NUTRITIONITEM_CREATE =
            "create table " + DiningContract.Nutrition.TABLE_NAME + " (" +
                    DiningContract.Nutrition._ID + " int primary key," +
                    DiningContract.Nutrition.NAME + " text," +
                    DiningContract.Nutrition.CALORIES + " text," +
                    DiningContract.Nutrition.ALCOHOL + " boolean," +
                    DiningContract.Nutrition.CARBOHYDRATES + " text," +
                    DiningContract.Nutrition.CHOLESTEROL + " text," +
                    DiningContract.Nutrition.DAIRY + " boolean," +
                    DiningContract.Nutrition.DIETARY_FIBER + " text," +
                    DiningContract.Nutrition.EGGS + " boolean," +
                    DiningContract.Nutrition.FAT + " text," +
                    DiningContract.Nutrition.FISH + " boolean," +
                    DiningContract.Nutrition.GLUTEN + " boolean," +
                    DiningContract.Nutrition.GLUTEN_FREE + " boolean," +
                    DiningContract.Nutrition.IRON + " text," +
                    DiningContract.Nutrition.NUTS + " boolean," +
                    DiningContract.Nutrition.PEANUT + " boolean," +
                    DiningContract.Nutrition.PORK + " boolean," +
                    DiningContract.Nutrition.PROTEIN + " text," +
                    DiningContract.Nutrition.SATURATED_FAT + " text," +
                    DiningContract.Nutrition.SERVING_SIZE + " text," +
                    DiningContract.Nutrition.SHELLFISH + " boolean," +
                    DiningContract.Nutrition.SOY + " boolean," +
                    DiningContract.Nutrition.SUGAR + " text," +
                    DiningContract.Nutrition.VEGAN + " boolean," +
                    DiningContract.Nutrition.VEGETARIAN + " boolean," +
                    DiningContract.Nutrition.VITAMIN_A + " text," +
                    DiningContract.Nutrition.VITAMIN_C + " text," +
                    DiningContract.Nutrition.WARNING + " text," +
                    DiningContract.Nutrition.WHEAT + " boolean" +
                    ");";

    private static final String SQL_INGREDIENT_CREATE =
            "create table " + DiningContract.Ingredient.TABLE_NAME + " (" +
                    DiningContract.Ingredient._ID + " int primary key," +
                    DiningContract.Ingredient.NAME + " text," +
                    DiningContract.Ingredient.NUTRITION_ID + " text," +
                    "foreign key (" + DiningContract.Ingredient.NUTRITION_ID + ") references " + DiningContract.Nutrition.TABLE_NAME +
                    "(" + DiningContract.Nutrition._ID + ")" + ");";

    private static final String SQL_DININGHALL_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.Location.TABLE_NAME;
    private static final String SQL_NUTRITIONITEM_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.Nutrition.TABLE_NAME;
    private static final String SQL_MENUITEM_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.Item.TABLE_NAME;
    private static final String SQL_INGREDIENT_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.Ingredient.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DININGHALL_CREATE);
        db.execSQL(SQL_NUTRITIONITEM_CREATE);
        db.execSQL(SQL_MENUITEM_CREATE);
        db.execSQL(SQL_INGREDIENT_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DININGHALL_DELETE);
        db.execSQL(SQL_NUTRITIONITEM_DELETE);
        db.execSQL(SQL_MENUITEM_DELETE);
        db.execSQL(SQL_INGREDIENT_DELETE);
        Log.d("SQL", "upgrade");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    void resetMenus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DININGHALL_DELETE);
        db.execSQL(SQL_DININGHALL_CREATE);
    }

    void reset() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DININGHALL_DELETE);
        db.execSQL(SQL_NUTRITIONITEM_DELETE);
        db.execSQL(SQL_MENUITEM_DELETE);
        db.execSQL(SQL_INGREDIENT_DELETE);
        onCreate(db);
    }

    public void insertHall(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DiningContract.Location.TABLE_NAME, null, values);
    }

    public void updateHall(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DiningContract.Location._ID)};
        int result = db.update(DiningContract.Location.TABLE_NAME, values, DiningContract.Location._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    void updateTime(int hallId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] args = {"" + hallId};
        values.put(DiningContract.Location.LAST_UPDATED, DateFormatProvider.date.format(new Date()));
        int result = db.update(DiningContract.Location.TABLE_NAME, values, DiningContract.Location._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    public void insertMenuItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DiningContract.Item.TABLE_NAME, null, values);
    }

    public void updateMenuItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DiningContract.Item._ID)};
        int result = db.update(DiningContract.Item.TABLE_NAME, values, DiningContract.Item._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    public void insertNutritionItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DiningContract.Nutrition.TABLE_NAME, null, values);
    }

    public void insertIngredient(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + DiningContract.Ingredient.TABLE_NAME + " where " + DiningContract.Ingredient.NUTRITION_ID + " = " +
                values.getAsInteger(DiningContract.Ingredient.NUTRITION_ID) + " and " + DiningContract.Ingredient.NAME + " = '" + values.getAsString(DiningContract.Ingredient.NAME) + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return;
        }
        db.insert(DiningContract.Ingredient.TABLE_NAME, null, values);
    }

    public boolean itemInDb(String tableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + tableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    Cursor getHall(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.Location.LAST_UPDATED + " from " + DiningContract.Location.TABLE_NAME + " where " + DiningContract.Location._ID + " = " + id;
        return db.rawQuery(Query, null);
    }

    Cursor getHalls() {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.Location.NAME + ", " + DiningContract.Location.CAPACITY + ", " + DiningContract.Location.LATITUDE + ", " + DiningContract.Location.LONGITUDE + ", " + DiningContract.Location._ID + ", " + DiningContract.Location.IS_CLOSED + " from " + DiningContract.Location.TABLE_NAME;
        return db.rawQuery(Query, null);
    }

    Cursor getMenu(int hall) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.Item.NAME + ", " + DiningContract.Item.MENU_NAME + ", " + DiningContract.Item.START_TIME + ", " + DiningContract.Item.END_TIME + ", " + DiningContract.Item.NUTRITION_ID + ", " + DiningContract.Item.DINING_HALL + " from " + DiningContract.Item.TABLE_NAME + " where " + DiningContract.Item.DINING_HALL + " = " + hall + " AND date('now', 'localtime') = date(" + DiningContract.Item.DATE + ") AND time('now', 'localtime') <= time(" + DiningContract.Item.END_TIME + ")";
        return db.rawQuery(Query, null);
    }

    Cursor getNutritionItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + DiningContract.Nutrition.TABLE_NAME + " where " + DiningContract.Nutrition._ID + " = " + id;
        return db.rawQuery(Query, null);
    }

    Cursor getIngredients(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.Ingredient.NAME + " from " + DiningContract.Ingredient.TABLE_NAME + " where " + DiningContract.Ingredient.NUTRITION_ID + " = " + id;
        return db.rawQuery(Query, null);
    }
}
