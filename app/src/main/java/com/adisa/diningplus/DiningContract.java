package com.adisa.diningplus;

import android.provider.BaseColumns;

/**
 * Created by Adisa on 3/19/2017.
 */

final class DiningContract {
    private DiningContract() {
    }

    static class DiningHall implements BaseColumns {
        static final String TABLE_NAME = "dining_hall";
        static final String NAME = "name";
        static final String TYPE = "type";
        static final String LATITUDE = "latitude";
        static final String LONGITUDE = "longitude";
        static final String MANAGER1_NAME = "manager1_name";
        static final String MANAGER1_EMAIL = "manager1_email";
        static final String MANAGER2_NAME = "manager2_name";
        static final String MANAGER2_EMAIL = "manager2_email";
        static final String MANAGER3_NAME = "manager3_name";
        static final String MANAGER3_EMAIL = "manager3_email";
        static final String MANAGER4_NAME = "manager4_name";
        static final String MANAGER4_EMAIL = "manager4_email";
        static final String CAPACITY = "capacity";
        static final String IS_CLOSED = "is_closed";
        static final String PHONE = "phone";
        static final String ADDRESS = "address";
        static final String LAST_UPDATED = "last_updated";
    }

    static class MenuItem implements BaseColumns {
        static final String TABLE_NAME = "menu_item";
        static final String NAME = "name";
        static final String DINING_HALL = "dining_hall";
        static final String MENU_NAME = "menu_name";
        static final String MENU_CODE = "menu_code";
        static final String DATE = "date";
        static final String START_TIME = "start_time";
        static final String END_TIME = "end_time";
        static final String NUTRITION_ID = "nutrition_id";
    }

    static class NutritionItem implements BaseColumns {
        static final String TABLE_NAME = "nutrition_item";
        static final String NAME = "name";
        static final String SERVING_SIZE = "serving_size";
        static final String CALORIES = "calories";
        static final String PROTEIN = "protein";
        static final String FAT = "fat";
        static final String SATURATED_FAT = "saturated_fat";
        static final String CHOLESTEROL = "cholesterol";
        static final String CARBOHYDRATES = "carbohydrates";
        static final String SUGAR = "sugar";
        static final String DIETARY_FIBER = "dietary_fiber";
        static final String VITAMIN_C = "vitamin_c";
        static final String VITAMIN_A = "vitamin_a";
        static final String IRON = "iron";
        static final String ALCOHOL = "alcohol";
        static final String NUTS = "nuts";
        static final String SHELLFISH = "shellfish";
        static final String PEANUT = "peanut";
        static final String DAIRY = "dairy";
        static final String EGGS = "eggs";
        static final String VEGAN = "vegan";
        static final String PORK = "pork";
        static final String FISH = "fish";
        static final String SOY = "soy";
        static final String WHEAT = "wheat";
        static final String GLUTEN = "gluten";
        static final String VEGETARIAN = "vegetarian";
        static final String GLUTEN_FREE = "gluten_free";
        static final String WARNING = "warning";
    }

    static class Ingredient implements BaseColumns {
        static final String TABLE_NAME = "ingredient";
        static final String NUTRITION_ID = "nutrition_id";
        static final String NAME = "name";
    }
}
