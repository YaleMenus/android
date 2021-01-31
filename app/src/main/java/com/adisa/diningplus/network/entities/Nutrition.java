package com.adisa.diningplus.network.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Nutrition {
    public String name;
    public String servingSize;
    public Integer calories;

    public String totalFat;
    public String saturatedFat;
    public String transFat;
    public String cholesterol;
    public String sodium;
    public String totalCarbohydrate;
    public String dietaryFiber;
    public String totalSugars;
    public String protein;
    public String vitaminD;
    public String vitaminA;
    public String vitaminC;
    public String calcium;
    public String iron;
    public String potassium;

    public Integer totalFatPDV;
    public Integer saturatedFatPDV;
    public Integer transFatPDV;
    public Integer cholesterolPDV;
    public Integer sodiumPDV;
    public Integer totalCarbohydratePDV;
    public Integer dietaryFiberPDV;
    public Integer totalSugarsPDV;
    public Integer proteinPDV;
    public Integer vitaminDPDV;
    public Integer vitaminAPDV;
    public Integer vitaminCPDV;
    public Integer calciumPDV;
    public Integer ironPDV;
    public Integer potassiumPDV;

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
