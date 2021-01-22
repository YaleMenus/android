package com.adisa.diningplus.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "items")
public class Item implements Serializable {
    @PrimaryKey
    public int id;
    public String name;
    public String ingredients;
    public String course;

    public boolean meat;
    public boolean animal_products;
    public boolean alcohol;
    public boolean nuts;
    public boolean shellfish;
    public boolean peanuts;
    public boolean dairy;
    public boolean egg;
    public boolean pork;
    public boolean fish;
    public boolean soy;
    public boolean wheat;
    public boolean gluten;
    public boolean coconut;

    @ColumnInfo(name = "meal_id")
    public int mealId;

    @Ignore
    public boolean restricted;

    public static Item fromJSON(JSONObject raw) throws JSONException {
        Item item = new Item();
        item.id = raw.getInt("id");
        item.name = raw.getString("name");
        item.course = raw.getString("course");
        item.ingredients = raw.getString("ingredients");
        item.meat = raw.getBoolean("meat");
        item.animal_products = raw.getBoolean("animal_products");
        item.alcohol = raw.getBoolean("alcohol");
        item.nuts = raw.getBoolean("nuts");
        item.shellfish = raw.getBoolean("shellfish");
        item.peanuts = raw.getBoolean("peanuts");
        item.dairy = raw.getBoolean("dairy");
        item.egg = raw.getBoolean("egg");
        item.pork = raw.getBoolean("pork");
        item.fish = raw.getBoolean("fish");
        item.soy = raw.getBoolean("soy");
        item.wheat = raw.getBoolean("wheat");
        item.gluten = raw.getBoolean("gluten");
        item.coconut = raw.getBoolean("coconut");
        return item;
    }
}
