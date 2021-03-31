package com.adisa.diningplus.activities

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.adisa.diningplus.R
import com.adisa.diningplus.adapters.NutritionAdapter
import com.adisa.diningplus.adapters.TraitAdapter
import com.adisa.diningplus.network.entities.Item
import com.adisa.diningplus.network.entities.Nutrition
import com.adisa.diningplus.fragments.FollowDialogFragment
import com.adisa.diningplus.network.API
import java.util.*

class ItemActivity : AppCompatActivity() {
    var api: API? = null
    var preferences: SharedPreferences? = null
    var itemName: String? = null
    var itemId = 0
    var showNutrition: Boolean = false
    var item: Item? = null
    var loaderView: View? = null
    var bodyView: View? = null
    var traits = ArrayList<Trait>()
    var nutrients = ArrayList<Nutrient>()
    var traitsView: LinearLayout? = null
    var nutrientsView: LinearLayout? = null
    var ingredientsView: TextView? = null
    var nutrition: Nutrition? = null

    inner class Trait internal constructor(var image: Int, var name: String)

    enum class NutrientType {
        HEADER, MAIN, SUB, PLAIN
    }

    inner class Nutrient {
        var type: NutrientType
        var name: String
        var amount: String
        var pdv: Int?

        internal constructor(type: NutrientType, name: String, amount: String) {
            this.type = type
            this.name = name
            this.amount = amount
            pdv = null
        }

        internal constructor(type: NutrientType, name: String, amount: String, pdv: Int?) {
            this.type = type
            this.name = name
            this.amount = amount
            this.pdv = pdv
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        api = API(this)
        val i = intent
        itemName = i.getStringExtra("name")
        itemId = i.getIntExtra("id", -1)
        loaderView = findViewById(R.id.loader)
        bodyView = findViewById(R.id.body)
        traitsView = findViewById(R.id.traits)
        nutrientsView = findViewById(R.id.nutrients)
        ingredientsView = findViewById(R.id.ingredients)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        supportActionBar!!.title = itemName
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getBoolean("follow_tutorial", true)) {
            val editor = preferences.edit()
            editor.putBoolean("follow_tutorial", false)
            editor.apply()
            val prev = supportFragmentManager.findFragmentByTag("follow")
            if (prev == null) {
                val followDialog: DialogFragment = FollowDialogFragment()
                followDialog.show(supportFragmentManager, "follow")
            }
        }
        showNutrition = preferences.getBoolean("show_nutrition", true);
        val itemTask = ItemTask()
        itemTask.execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        val current = preferences!!.getStringSet("followed_items", HashSet()) as HashSet<String>?
        if (current!!.contains(itemName)) {
            menu.findItem(R.id.action_notify).setIcon(R.drawable.notifications_enabled)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private inner class ItemTask : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            item = null
            item = api!!.getItem(itemId)

            if (item!!.meat) traits.add(Trait(R.drawable.trait_meat, "Meat"))
            if (item!!.animalProducts) traits.add(Trait(R.drawable.trait_animal_products, "Animal Products"))
            if (item!!.alcohol) traits.add(Trait(R.drawable.trait_alcohol, "Alcohol"))
            if (item!!.treeNut) traits.add(Trait(R.drawable.trait_tree_nut, "Tree Nut"))
            if (item!!.shellfish) traits.add(Trait(R.drawable.trait_shellfish, "Shellfish"))
            if (item!!.peanuts) traits.add(Trait(R.drawable.trait_peanuts, "Peanuts"))
            if (item!!.dairy) traits.add(Trait(R.drawable.trait_dairy, "Dairy"))
            if (item!!.egg) traits.add(Trait(R.drawable.trait_egg, "Egg"))
            if (item!!.pork) traits.add(Trait(R.drawable.trait_pork, "Pork"))
            if (item!!.fish) traits.add(Trait(R.drawable.trait_fish, "Fish"))
            if (item!!.soy) traits.add(Trait(R.drawable.trait_soy, "Soy"))
            if (item!!.wheat) traits.add(Trait(R.drawable.trait_wheat, "Wheat"))
            if (item!!.gluten) traits.add(Trait(R.drawable.trait_gluten, "Gluten"))
            if (item!!.coconut) traits.add(Trait(R.drawable.trait_coconut, "Coconut"))

            if (showNutrition) {
                nutrition = api!!.getItemNutrition(itemId)
                nutrients.add(Nutrient(NutrientType.HEADER, "Serving Size", nutrition!!.servingSize))
                nutrients.add(Nutrient(NutrientType.HEADER, "Calories", nutrition!!.calories.toString()))
                nutrients.add(Nutrient(NutrientType.MAIN, "Total Fat", nutrition!!.totalFat, nutrition!!.totalFatPDV))
                nutrients.add(Nutrient(NutrientType.SUB, "Saturated Fat", nutrition!!.saturatedFat, nutrition!!.saturatedFatPDV))
                nutrients.add(Nutrient(NutrientType.SUB, "Trans Fat", nutrition!!.transFat, nutrition!!.transFatPDV))
                nutrients.add(Nutrient(NutrientType.MAIN, "Cholesterol", nutrition!!.cholesterol, nutrition!!.cholesterolPDV))
                nutrients.add(Nutrient(NutrientType.MAIN, "Sodium", nutrition!!.sodium, nutrition!!.sodiumPDV))
                nutrients.add(Nutrient(NutrientType.MAIN, "Total Carbohydrate", nutrition!!.totalCarbohydrate, nutrition!!.totalCarbohydratePDV))
                nutrients.add(Nutrient(NutrientType.SUB, "Dietary Fiber", nutrition!!.dietaryFiber, nutrition!!.dietaryFiberPDV))
                nutrients.add(Nutrient(NutrientType.SUB, "Total Sugars", nutrition!!.totalSugars, nutrition!!.totalSugarsPDV))
                nutrients.add(Nutrient(NutrientType.MAIN, "Protein", nutrition!!.protein, nutrition!!.proteinPDV))
                nutrients.add(Nutrient(NutrientType.PLAIN, "Vitamin D", nutrition!!.vitaminD, nutrition!!.vitaminDPDV))
                nutrients.add(Nutrient(NutrientType.PLAIN, "Vitamin A", nutrition!!.vitaminA, nutrition!!.vitaminAPDV))
                nutrients.add(Nutrient(NutrientType.PLAIN, "Vitamin C", nutrition!!.vitaminC, nutrition!!.vitaminCPDV))
                nutrients.add(Nutrient(NutrientType.PLAIN, "Calcium", nutrition!!.calcium, nutrition!!.calciumPDV))
                nutrients.add(Nutrient(NutrientType.PLAIN, "Iron", nutrition!!.iron, nutrition!!.ironPDV))
                nutrients.add(Nutrient(NutrientType.PLAIN, "Potassium", nutrition!!.potassium, nutrition!!.potassiumPDV))
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            val traitAdapter = TraitAdapter(this@ItemActivity, traits)
            // TODO: figure out a way to make this automatic through adapter. Right now we're doing this so we can use LinearLayout instead of ListAdapter.
            for (position in 0 until traitAdapter.count) {
                traitsView!!.addView(traitAdapter.getView(position, null, null))
            }
            val nutritionAdapter = NutritionAdapter(this@ItemActivity, nutrients)
            if (showNutrition) {
                for (position in 0 until nutritionAdapter.count) {
                    nutrientsView!!.addView(nutritionAdapter.getView(position, null, null))
                }
            } else {
                nutrientsView!!.visibility = View.GONE
            }
            ingredientsView!!.text = "Ingredients: " + item!!.ingredients
            bodyView!!.visibility = View.VISIBLE
            loaderView!!.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_notify -> {
                val editor = preferences!!.edit()
                val currentSet = preferences!!.getStringSet("followed_items", HashSet()) as HashSet<String>?
                val newSet = HashSet<String?>()
                newSet.addAll(currentSet!!)
                if (!currentSet.contains(itemName)) {
                    item.setIcon(R.drawable.notifications_enabled)
                    newSet.add(itemName)
                } else {
                    item.setIcon(R.drawable.notifications_disabled)
                    newSet.remove(itemName)
                }
                editor.putStringSet("followed_items", newSet)
                editor.apply()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}