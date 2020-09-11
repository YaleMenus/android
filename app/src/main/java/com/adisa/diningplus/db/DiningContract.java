package com.adisa.diningplus.db;

import android.provider.BaseColumns;

public class DiningContract {
    private DiningContract() {
    }

    public static class Location implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String CAPACITY = "capacity";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String IS_CLOSED = "is_closed";
        public static final String PHONE = "phone";
        public static final String ADDRESS = "address";
        public static final String LAST_UPDATED = "last_updated";
    }

    public static class Manager implements BaseColumns {
        public static final String TABLE_NAME = "managers";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
    }

    public static class Meal implements BaseColumns {
        public static final String TABLE_NAME = "meals";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DATE = "date";
        public static final String LOCATION_ID = "location_id";
    }

    public static class Course implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String MEAL_ID = "meal_id";
    }

    public static class Item implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String NAME = "name";
        public static final String INGREDIENTS = "ingredients";

        public static final String VEGETARIAN = "vegetarian";
        public static final String VEGAN = "vegan";

        public static final String ALCOHOL = "alcohol";
        public static final String NUTS = "nuts";
        public static final String SHELLFISH = "shellfish";
        public static final String PEANUTS = "peanuts";
        public static final String DAIRY = "dairy";
        public static final String EGG = "egg";
        public static final String PORK = "pork";
        public static final String SEAFOOD = "seafood";
        public static final String SOY = "soy";
        public static final String WHEAT = "wheat";
        public static final String GLUTEN = "gluten";
        public static final String COCONUT = "coconut";
    }

    public static class Nutrition implements BaseColumns {
        public static final String TABLE_NAME = "nutrition";
        public static final String NAME = "name";
        public static final String PORTION_SIZE = "portion_size";
        public static final String CALORIES = "calories";

        public static final String TOTAL_FAT = "total_fat";
        public static final String SATURATED_FAT = "saturated_fat";
        public static final String TRANS_FAT = "trans_fat";
        public static final String CHOLESTEROL = "cholesterol";
        public static final String SODIUM = "sodium";
        public static final String TOTAL_CARBOHYDRATE = "total_carbohydrate";
        public static final String DIETARY_FIBER = "dietary_fiber";
        public static final String TOTAL_SUGARS = "total_sugars";
        public static final String PROTEIN = "protein";
        public static final String VITAMIN_D = "vitamin_d";
        public static final String VITAMIN_A = "vitamin_a";
        public static final String VITAMIN_C = "vitamin_c";
        public static final String CALCIUM = "calcium";
        public static final String IRON = "iron";
        public static final String POTASSIUM = "potassium";

        public static final String TOTAL_FAT_PDV = "total_fat_pdv";
        public static final String SATURATED_FAT_PDV = "saturated_fat_pdv";
        public static final String TRANS_FAT_PDV = "trans_fat_pdv";
        public static final String CHOLESTEROL_PDV = "cholesterol_pdv";
        public static final String SODIUM_PDV = "sodium_pdv";
        public static final String TOTAL_CARBOHYDRATE_PDV = "total_carbohydrate_pdv";
        public static final String DIETARY_FIBER_PDV = "dietary_fiber_pdv";
        public static final String TOTAL_SUGARS_PDV = "total_sugars_pdv";
        public static final String PROTEIN_PDV = "protein_pdv";
        public static final String VITAMIN_D_PDV = "vitamin_d_pdv";
        public static final String VITAMIN_A_PDV = "vitamin_a_pdv";
        public static final String VITAMIN_C_PDV = "vitamin_c_pdv";
        public static final String CALCIUM_PDV = "calcium_pdv";
        public static final String IRON_PDV = "iron_pdv";
        public static final String POTASSIUM_PDV = "potassium_pdv";

        public static final String ITEM_ID = "item_id";
    }
}
