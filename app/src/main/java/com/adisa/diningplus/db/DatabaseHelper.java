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
            "create table " + DatabaseContract.Location.TABLE_NAME + " (" +
                    DatabaseContract.Location._ID + " int primary key," +
                    DatabaseContract.Location.NAME + " text," +
                    DatabaseContract.Location.ADDRESS + " text," +
                    DatabaseContract.Location.CAPACITY + " int," +
                    DatabaseContract.Location.IS_CLOSED + " boolean," +
                    DatabaseContract.Location.LATITUDE + " text," +
                    DatabaseContract.Location.LONGITUDE + " text," +
                    DatabaseContract.Location.MANAGER1_EMAIL + " text," +
                    DatabaseContract.Location.MANAGER1_NAME + " text," +
                    DatabaseContract.Location.MANAGER2_EMAIL + " text," +
                    DatabaseContract.Location.MANAGER2_NAME + " text," +
                    DatabaseContract.Location.MANAGER3_EMAIL + " text," +
                    DatabaseContract.Location.MANAGER3_NAME + " text," +
                    DatabaseContract.Location.MANAGER4_EMAIL + " text," +
                    DatabaseContract.Location.MANAGER4_NAME + " text," +
                    DatabaseContract.Location.PHONE + " text," +
                    DatabaseContract.Location.TYPE + " text," +
                    DatabaseContract.Location.LAST_UPDATED + " text" +
                    ");";

    private static final String SQL_MENUITEM_CREATE =
            "create table " + DatabaseContract.Item.TABLE_NAME + " (" +
                    DatabaseContract.Item._ID + " int primary key," +
                    DatabaseContract.Item.NAME + " text," +
                    DatabaseContract.Item.DINING_HALL + " int," +
                    DatabaseContract.Item.MENU_NAME + " text," +
                    DatabaseContract.Item.MENU_CODE + " int," +
                    DatabaseContract.Item.DATE + " text," +
                    DatabaseContract.Item.START_TIME + " text," +
                    DatabaseContract.Item.END_TIME + " text," +
                    DatabaseContract.Item.NUTRITION_ID + " text," +
                    "foreign key (" + DatabaseContract.Item.DINING_HALL + ") references " + DatabaseContract.Location.TABLE_NAME + "("
                    + DatabaseContract.Location._ID + ")," +
                    "foreign key (" + DatabaseContract.Item.NUTRITION_ID + ") references " + DatabaseContract.Nutrition.TABLE_NAME +
                    "(" + DatabaseContract.Nutrition._ID + ")" +
                    ");";

    private static final String SQL_NUTRITIONITEM_CREATE =
            "create table " + DatabaseContract.Nutrition.TABLE_NAME + " (" +
                    DatabaseContract.Nutrition._ID + " int primary key," +
                    DatabaseContract.Nutrition.NAME + " text," +
                    DatabaseContract.Nutrition.CALORIES + " text," +
                    DatabaseContract.Nutrition.ALCOHOL + " boolean," +
                    DatabaseContract.Nutrition.CARBOHYDRATES + " text," +
                    DatabaseContract.Nutrition.CHOLESTEROL + " text," +
                    DatabaseContract.Nutrition.DAIRY + " boolean," +
                    DatabaseContract.Nutrition.DIETARY_FIBER + " text," +
                    DatabaseContract.Nutrition.EGGS + " boolean," +
                    DatabaseContract.Nutrition.FAT + " text," +
                    DatabaseContract.Nutrition.FISH + " boolean," +
                    DatabaseContract.Nutrition.GLUTEN + " boolean," +
                    DatabaseContract.Nutrition.GLUTEN_FREE + " boolean," +
                    DatabaseContract.Nutrition.IRON + " text," +
                    DatabaseContract.Nutrition.NUTS + " boolean," +
                    DatabaseContract.Nutrition.PEANUT + " boolean," +
                    DatabaseContract.Nutrition.PORK + " boolean," +
                    DatabaseContract.Nutrition.PROTEIN + " text," +
                    DatabaseContract.Nutrition.SATURATED_FAT + " text," +
                    DatabaseContract.Nutrition.SERVING_SIZE + " text," +
                    DatabaseContract.Nutrition.SHELLFISH + " boolean," +
                    DatabaseContract.Nutrition.SOY + " boolean," +
                    DatabaseContract.Nutrition.SUGAR + " text," +
                    DatabaseContract.Nutrition.VEGAN + " boolean," +
                    DatabaseContract.Nutrition.VEGETARIAN + " boolean," +
                    DatabaseContract.Nutrition.VITAMIN_A + " text," +
                    DatabaseContract.Nutrition.VITAMIN_C + " text," +
                    DatabaseContract.Nutrition.WARNING + " text," +
                    DatabaseContract.Nutrition.WHEAT + " boolean" +
                    ");";

    private static final String SQL_INGREDIENT_CREATE =
            "create table " + DatabaseContract.Ingredient.TABLE_NAME + " (" +
                    DatabaseContract.Ingredient._ID + " int primary key," +
                    DatabaseContract.Ingredient.NAME + " text," +
                    DatabaseContract.Ingredient.NUTRITION_ID + " text," +
                    "foreign key (" + DatabaseContract.Ingredient.NUTRITION_ID + ") references " + DatabaseContract.Nutrition.TABLE_NAME +
                    "(" + DatabaseContract.Nutrition._ID + ")" + ");";

    private static final String SQL_DININGHALL_DELETE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Location.TABLE_NAME;
    private static final String SQL_NUTRITIONITEM_DELETE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Nutrition.TABLE_NAME;
    private static final String SQL_MENUITEM_DELETE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Item.TABLE_NAME;
    private static final String SQL_INGREDIENT_DELETE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Ingredient.TABLE_NAME;

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
        db.insert(DatabaseContract.Location.TABLE_NAME, null, values);
    }

    public void updateHall(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Location._ID)};
        int result = db.update(DatabaseContract.Location.TABLE_NAME, values, DatabaseContract.Location._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    void updateTime(int hallId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] args = {"" + hallId};
        values.put(DatabaseContract.Location.LAST_UPDATED, DateFormatProvider.date.format(new Date()));
        int result = db.update(DatabaseContract.Location.TABLE_NAME, values, DatabaseContract.Location._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    public void insertMenuItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Item.TABLE_NAME, null, values);
    }

    public void updateMenuItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {"" + values.getAsInteger(DatabaseContract.Item._ID)};
        int result = db.update(DatabaseContract.Item.TABLE_NAME, values, DatabaseContract.Item._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    public void insertNutritionItem(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContract.Nutrition.TABLE_NAME, null, values);
    }

    public void insertIngredient(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + DatabaseContract.Ingredient.TABLE_NAME + " where " + DatabaseContract.Ingredient.NUTRITION_ID + " = " +
                values.getAsInteger(DatabaseContract.Ingredient.NUTRITION_ID) + " and " + DatabaseContract.Ingredient.NAME + " = '" + values.getAsString(DatabaseContract.Ingredient.NAME) + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return;
        }
        db.insert(DatabaseContract.Ingredient.TABLE_NAME, null, values);
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
        String Query = "Select " + DatabaseContract.Location.LAST_UPDATED + " from " + DatabaseContract.Location.TABLE_NAME + " where " + DatabaseContract.Location._ID + " = " + id;
        return db.rawQuery(Query, null);
    }

    Cursor getHalls() {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DatabaseContract.Location.NAME + ", " + DatabaseContract.Location.CAPACITY + ", " + DatabaseContract.Location.LATITUDE + ", " + DatabaseContract.Location.LONGITUDE + ", " + DatabaseContract.Location._ID + ", " + DatabaseContract.Location.IS_CLOSED + " from " + DatabaseContract.Location.TABLE_NAME;
        return db.rawQuery(Query, null);
    }

    Cursor getMenu(int hall) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DatabaseContract.Item.NAME + ", " + DatabaseContract.Item.MENU_NAME + ", " + DatabaseContract.Item.START_TIME + ", " + DatabaseContract.Item.END_TIME + ", " + DatabaseContract.Item.NUTRITION_ID + ", " + DatabaseContract.Item.DINING_HALL + " from " + DatabaseContract.Item.TABLE_NAME + " where " + DatabaseContract.Item.DINING_HALL + " = " + hall + " AND date('now', 'localtime') = date(" + DatabaseContract.Item.DATE + ") AND time('now', 'localtime') <= time(" + DatabaseContract.Item.END_TIME + ")";
        return db.rawQuery(Query, null);
    }

    Cursor getNutritionItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + DatabaseContract.Nutrition.TABLE_NAME + " where " + DatabaseContract.Nutrition._ID + " = " + id;
        return db.rawQuery(Query, null);
    }

    Cursor getIngredients(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DatabaseContract.Ingredient.NAME + " from " + DatabaseContract.Ingredient.TABLE_NAME + " where " + DatabaseContract.Ingredient.NUTRITION_ID + " = " + id;
        return db.rawQuery(Query, null);
    }
}
