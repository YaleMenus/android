package com.adisa.diningplus.network.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "nutrition")
public class Nutrition implements Serializable {
    public String name;
    @ColumnInfo(name = "serving_size")
    public String servingSize;
    public Integer calories;

    @ColumnInfo(name="total_fat")
    public String totalFat;
    @ColumnInfo(name="saturated_fat")
    public String saturatedFat;
    @ColumnInfo(name="trans_fat")
    public String transFat;
    public String cholesterol;
    public String sodium;
    @ColumnInfo(name="total_carbohydrate")
    public String totalCarbohydrate;
    @ColumnInfo(name="dietary_fiber")
    public String dietaryFiber;
    @ColumnInfo(name="total_sugars")
    public String totalSugars;
    public String protein;
    @ColumnInfo(name="vitamin_d")
    public String vitaminD;
    @ColumnInfo(name="vitamin_a")
    public String vitaminA;
    @ColumnInfo(name="vitamin_c")
    public String vitaminC;
    public String calcium;
    public String iron;
    public String potassium;

    @ColumnInfo(name="total_fat_pdv")
    public Integer totalFatPDV;
    @ColumnInfo(name="saturated_fat_pdv")
    public Integer saturatedFatPDV;
    @ColumnInfo(name="trans_fat_pdv")
    public Integer transFatPDV;
    @ColumnInfo(name="cholesterol_pdv")
    public Integer cholesterolPDV;
    @ColumnInfo(name="sodium_pdv")
    public Integer sodiumPDV;
    @ColumnInfo(name="total_carbohydrate_pdv")
    public Integer totalCarbohydratePDV;
    @ColumnInfo(name="dietary_fiber_pdv")
    public Integer dietaryFiberPDV;
    @ColumnInfo(name="total_sugars_pdv")
    public Integer totalSugarsPDV;
    @ColumnInfo(name="protein_pdv")
    public Integer proteinPDV;
    @ColumnInfo(name="vitamin_d_pdv")
    public Integer vitaminDPDV;
    @ColumnInfo(name="vitamin_a_pdv")
    public Integer vitaminAPDV;
    @ColumnInfo(name="vitamin_c_pdv")
    public Integer vitaminCPDV;
    @ColumnInfo(name="calcium_pdv")
    public Integer calciumPDV;
    @ColumnInfo(name="iron_pdv")
    public Integer ironPDV;
    @ColumnInfo(name="potassium_pdv")
    public Integer potassiumPDV;

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    public int itemId;

    public static Nutrition fromJSON(JSONObject raw) throws JSONException {
        Nutrition nutrition = new Nutrition();
        nutrition.servingSize = raw.getString("serving_size");
        nutrition.calories = raw.getInt("calories");
        nutrition.totalFat = raw.optString("total_fat");
        nutrition.saturatedFat = raw.optString("saturated_fat");
        nutrition.transFat = raw.optString("trans_fat");
        nutrition.cholesterol = raw.optString("cholesterol");
        nutrition.sodium = raw.optString("sodium");
        nutrition.totalCarbohydrate = raw.optString("total_carbohydrate");
        nutrition.dietaryFiber = raw.optString("dietary_fiber");
        nutrition.totalSugars = raw.optString("total_sugars");
        nutrition.protein = raw.optString("protein");
        nutrition.vitaminD = raw.optString("vitamin_d");
        nutrition.vitaminA = raw.optString("vitamin_a");
        nutrition.vitaminC = raw.optString("vitamin_c");
        nutrition.calcium = raw.optString("calcium");
        nutrition.iron = raw.optString("iron");
        nutrition.potassium = raw.optString("potassium");
        nutrition.totalFatPDV = raw.optInt("total_fat_pdv");
        nutrition.saturatedFatPDV = raw.optInt("saturated_fat_pdv");
        nutrition.transFatPDV = raw.optInt("trans_fat_pdv");
        nutrition.cholesterolPDV = raw.optInt("cholesterol_pdv");
        nutrition.sodiumPDV = raw.optInt("sodium_pdv");
        nutrition.totalCarbohydratePDV = raw.optInt("total_carbohydrate_pdv");
        nutrition.dietaryFiberPDV = raw.optInt("dietary_fiber_pdv");
        nutrition.totalSugarsPDV = raw.optInt("total_sugars_pdv");
        nutrition.proteinPDV = raw.optInt("protein_pdv");
        nutrition.vitaminDPDV = raw.optInt("vitamin_d_pdv");
        nutrition.vitaminAPDV = raw.optInt("vitamin_a_pdv");
        nutrition.vitaminCPDV = raw.optInt("vitamin_c_pdv");
        nutrition.calciumPDV = raw.optInt("calcium_pdv");
        nutrition.ironPDV = raw.optInt("iron_pdv");
        nutrition.potassiumPDV = raw.optInt("potassium_pdv");
        nutrition.itemId = raw.getInt("item_id");
        return nutrition;
    }
}
