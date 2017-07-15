package com.adisa.diningplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adisa on 3/20/2017.
 */

class DiningDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Dining.db";
    private static final String SQL_DININGHALL_CREATE =
            "create table " + DiningContract.DiningHall.TABLE_NAME + " (" +
                    DiningContract.DiningHall._ID + " int primary key," +
                    DiningContract.DiningHall.NAME + " text," +
                    DiningContract.DiningHall.ADDRESS + " text," +
                    DiningContract.DiningHall.CAPACITY + " int," +
                    DiningContract.DiningHall.IS_CLOSED + " boolean," +
                    DiningContract.DiningHall.LATITUDE + " text," +
                    DiningContract.DiningHall.LONGITUDE + " text," +
                    DiningContract.DiningHall.MANAGER1_EMAIL + " text," +
                    DiningContract.DiningHall.MANAGER1_NAME + " text," +
                    DiningContract.DiningHall.MANAGER2_EMAIL + " text," +
                    DiningContract.DiningHall.MANAGER2_NAME + " text," +
                    DiningContract.DiningHall.MANAGER3_EMAIL + " text," +
                    DiningContract.DiningHall.MANAGER3_NAME + " text," +
                    DiningContract.DiningHall.MANAGER4_EMAIL + " text," +
                    DiningContract.DiningHall.MANAGER4_NAME + " text," +
                    DiningContract.DiningHall.PHONE + " text," +
                    DiningContract.DiningHall.TYPE + " text," +
                    DiningContract.DiningHall.LAST_UPDATED + " text" +
                    ");";

    private static final String SQL_MENUITEM_CREATE =
            "create table " + DiningContract.MenuItem.TABLE_NAME + " (" +
                    DiningContract.MenuItem._ID + " int primary key," +
                    DiningContract.MenuItem.NAME + " text," +
                    DiningContract.MenuItem.DINING_HALL + " int," +
                    DiningContract.MenuItem.MENU_NAME + " text," +
                    DiningContract.MenuItem.MENU_CODE + " int," +
                    DiningContract.MenuItem.DATE + " text," +
                    DiningContract.MenuItem.START_TIME + " text," +
                    DiningContract.MenuItem.END_TIME + " text," +
                    DiningContract.MenuItem.NUTRITION_ID + " text," +
                    "foreign key (" + DiningContract.MenuItem.DINING_HALL + ") references " + DiningContract.DiningHall.TABLE_NAME + "("
                        + DiningContract.DiningHall._ID + ")," +
                    "foreign key (" + DiningContract.MenuItem.NUTRITION_ID + ") references " + DiningContract.NutritionItem.TABLE_NAME +
                        "(" + DiningContract.NutritionItem._ID + ")" +
                    ");";

    private static final String SQL_NUTRITIONITEM_CREATE =
            "create table " + DiningContract.NutritionItem.TABLE_NAME + " (" +
                    DiningContract.NutritionItem._ID + " int primary key," +
                    DiningContract.NutritionItem.NAME + " text," +
                    DiningContract.NutritionItem.CALORIES + " text," +
                    DiningContract.NutritionItem.ALCOHOL + " boolean," +
                    DiningContract.NutritionItem.CARBOHYDRATES + " text," +
                    DiningContract.NutritionItem.CHOLESTEROL + " text," +
                    DiningContract.NutritionItem.DAIRY + " boolean," +
                    DiningContract.NutritionItem.DIETARY_FIBER + " text," +
                    DiningContract.NutritionItem.EGGS + " boolean," +
                    DiningContract.NutritionItem.FAT + " text," +
                    DiningContract.NutritionItem.FISH + " boolean," +
                    DiningContract.NutritionItem.GLUTEN + " boolean," +
                    DiningContract.NutritionItem.GLUTEN_FREE + " boolean," +
                    DiningContract.NutritionItem.IRON + " text," +
                    DiningContract.NutritionItem.NUTS + " boolean," +
                    DiningContract.NutritionItem.PEANUT + " boolean," +
                    DiningContract.NutritionItem.PORK + " boolean," +
                    DiningContract.NutritionItem.PROTEIN + " text," +
                    DiningContract.NutritionItem.SATURATED_FAT + " text," +
                    DiningContract.NutritionItem.SERVING_SIZE + " text," +
                    DiningContract.NutritionItem.SHELLFISH + " boolean," +
                    DiningContract.NutritionItem.SOY + " boolean," +
                    DiningContract.NutritionItem.SUGAR + " text," +
                    DiningContract.NutritionItem.VEGAN + " boolean," +
                    DiningContract.NutritionItem.VEGETARIAN + " boolean," +
                    DiningContract.NutritionItem.VITAMIN_A + " text," +
                    DiningContract.NutritionItem.VITAMIN_C + " text," +
                    DiningContract.NutritionItem.WARNING + " text," +
                    DiningContract.NutritionItem.WHEAT + " boolean" +
                    ");";

    private static final String SQL_INGREDIENT_CREATE =
            "create table " + DiningContract.Ingredient.TABLE_NAME + " (" +
                    DiningContract.Ingredient._ID + " int primary key," +
                    DiningContract.Ingredient.NAME + " text," +
                    DiningContract.Ingredient.NUTRITION_ID + " text," +
                    "foreign key (" + DiningContract.Ingredient.NUTRITION_ID + ") references " + DiningContract.NutritionItem.TABLE_NAME +
                    "(" + DiningContract.NutritionItem._ID + ")" + ");";

    private static final String SQL_DININGHALL_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.DiningHall.TABLE_NAME;
    private static final String SQL_NUTRITIONITEM_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.NutritionItem.TABLE_NAME;
    private static final String SQL_MENUITEM_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.MenuItem.TABLE_NAME;
    private static final String SQL_INGREDIENT_DELETE =
            "DROP TABLE IF EXISTS " + DiningContract.Ingredient.TABLE_NAME;

    DiningDbHelper(Context context) {
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

    void resetMenus(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DININGHALL_DELETE);
        db.execSQL(SQL_DININGHALL_CREATE);
    }

    void reset(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DININGHALL_DELETE);
        db.execSQL(SQL_NUTRITIONITEM_DELETE);
        db.execSQL(SQL_MENUITEM_DELETE);
        db.execSQL(SQL_INGREDIENT_DELETE);
        onCreate(db);
    }
    void insertHall(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DiningContract.DiningHall.TABLE_NAME, null, values);
    }

    void updateHall(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        String [] args =  {"" + values.getAsInteger(DiningContract.DiningHall._ID)};
        int result = db.update(DiningContract.DiningHall.TABLE_NAME, values, DiningContract.DiningHall._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    void updateTime(int hallId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        String [] args =  {"" + hallId};
        values.put(DiningContract.DiningHall.LAST_UPDATED,  new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        int result = db.update(DiningContract.DiningHall.TABLE_NAME, values, DiningContract.DiningHall._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    void insertMenuItem(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DiningContract.MenuItem.TABLE_NAME, null, values);
    }

    void updateMenuItem(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        String [] args =  {"" + values.getAsInteger(DiningContract.MenuItem._ID)};
        int result = db.update(DiningContract.MenuItem.TABLE_NAME, values, DiningContract.MenuItem._ID + " = ?", args);
        if (BuildConfig.DEBUG && result != 1)
            throw new AssertionError();
    }

    void insertNutritionItem(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DiningContract.NutritionItem.TABLE_NAME, null, values);
    }

    void insertIngredient(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + DiningContract.Ingredient.TABLE_NAME + " where " + DiningContract.Ingredient.NUTRITION_ID + " = " +
                values.getAsInteger(DiningContract.Ingredient.NUTRITION_ID) + " and " + DiningContract.Ingredient.NAME + " = '" + values.getAsString(DiningContract.Ingredient.NAME) + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() > 0){
            cursor.close();
            return;
        }
        db.insert(DiningContract.Ingredient.TABLE_NAME, null, values);
    }

    boolean itemInDb(String tableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + tableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    Cursor getHall(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.DiningHall.LAST_UPDATED + " from " + DiningContract.DiningHall.TABLE_NAME + " where " + DiningContract.DiningHall._ID + " = " + id;
        return db.rawQuery(Query, null);
    }

    Cursor getHalls(){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.DiningHall.NAME + ", " + DiningContract.DiningHall.CAPACITY  + ", " + DiningContract.DiningHall.LATITUDE + ", " +  DiningContract.DiningHall.LONGITUDE + ", " + DiningContract.DiningHall._ID + ", " + DiningContract.DiningHall.IS_CLOSED + " from " + DiningContract.DiningHall.TABLE_NAME;
        return db.rawQuery(Query, null);
    }

    Cursor getMenu(int hall) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.MenuItem.NAME + ", " + DiningContract.MenuItem.MENU_NAME + ", " + DiningContract.MenuItem.START_TIME + ", " + DiningContract.MenuItem.END_TIME + ", " + DiningContract.MenuItem.NUTRITION_ID + ", " + DiningContract.MenuItem.DINING_HALL + " from " + DiningContract.MenuItem.TABLE_NAME + " where " + DiningContract.MenuItem.DINING_HALL + " = " + hall + " AND date('now', 'localtime') = date(" + DiningContract.MenuItem.DATE + ") AND time('now', 'localtime') <= time(" + DiningContract.MenuItem.END_TIME + ")";
        return db.rawQuery(Query, null);
    }

    Cursor getNutritionItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + DiningContract.NutritionItem.TABLE_NAME + " where " + DiningContract.NutritionItem._ID + " = " + id;
        return db.rawQuery(Query, null);
    }

    Cursor getIngredients(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select " + DiningContract.Ingredient.NAME + " from " + DiningContract.Ingredient.TABLE_NAME + " where " + DiningContract.Ingredient.NUTRITION_ID + " = " + id;
        return db.rawQuery(Query, null);
    }
}
