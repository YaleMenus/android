package com.adisa.diningplus.activities

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.adisa.diningplus.R
import com.adisa.diningplus.adapters.NutritionAdapter
import com.adisa.diningplus.adapters.TraitAdapter
import com.adisa.diningplus.db.entities.Item
import com.adisa.diningplus.db.entities.Nutrition
import com.adisa.diningplus.fragments.FollowDialogFragment
import com.adisa.diningplus.network.API
import java.util.*

class ItemActivity : AppCompatActivity() {
    var api: API? = null
    var preferences: SharedPreferences? = null
    var itemName: String? = null
    var itemId = 0
    var item: Item? = null
    var loaderView: View? = null
    var bodyView: View? = null
    var traits = ArrayList<Trait>()
    var nutrients = ArrayList<Nutrient>()
    var traitsView: ListView? = null
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
        traitsView = findViewById<View>(R.id.traits) as ListView
        nutrientsView = findViewById(R.id.nutrients)
        ingredientsView = findViewById(R.id.ingredients)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        supportActionBar!!.title = itemName
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getBoolean("followTutorial", true)) {
            val editor = preferences.edit()
            editor.putBoolean("followTutorial", false)
            editor.apply()
            val prev = supportFragmentManager.findFragmentByTag("follow")
            if (prev == null) {
                val followDialog: DialogFragment = FollowDialogFragment()
                followDialog.show(supportFragmentManager, "follow")
            }
        }
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
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            item = null
            item = api!!.getItem(itemId)
            // TODO: use meat/animal product icons
            if (!item!!.meat) traits.add(Trait(R.drawable.key_vegetarian, "Vegetarian"))
            if (!item!!.animalProducts) traits.add(Trait(R.drawable.key_vegan, "Vegan"))
            if (item!!.alcohol) traits.add(Trait(R.drawable.key_alcohol, "Alcohol"))
            if (item!!.nuts) traits.add(Trait(R.drawable.key_nuts, "Nuts"))
            if (item!!.shellfish) traits.add(Trait(R.drawable.key_shellfish, "Shellfish"))
            if (item!!.peanuts) traits.add(Trait(R.drawable.key_peanuts, "Peanuts"))
            if (item!!.dairy) traits.add(Trait(R.drawable.key_dairy, "Dairy"))
            if (item!!.egg) traits.add(Trait(R.drawable.key_egg, "Egg"))
            if (item!!.pork) traits.add(Trait(R.drawable.key_pork, "Pork"))
            if (item!!.fish) traits.add(Trait(R.drawable.key_fish, "Fish"))
            if (item!!.soy) traits.add(Trait(R.drawable.key_soy, "Soy"))
            if (item!!.wheat) traits.add(Trait(R.drawable.key_wheat, "Wheat"))
            if (item!!.gluten) traits.add(Trait(R.drawable.key_gluten, "Gluten"))
            if (item!!.coconut) traits.add(Trait(R.drawable.key_coconut, "Coconut"))
            nutrition = api!!.getItemNutrition(itemId)
            nutrients.add(Nutrient(NutrientType.HEADER,"Serving Size", nutrition!!.portionSize))
            nutrients.add(Nutrient(NutrientType.HEADER,"Calories", nutrition!!.calories))
            nutrients.add(Nutrient(NutrientType.MAIN,"Total Fat", nutrition!!.totalFat, nutrition!!.totalFatPDV))
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
            return null
        }

        override fun onPostExecute(result: Void?) {
            val traitAdapter = TraitAdapter(this@ItemActivity, traits)
            traitsView!!.adapter = traitAdapter
            val nutritionAdapter = NutritionAdapter(this@ItemActivity, nutrients)
            for (position in 0 until nutritionAdapter.count) {
                nutrientsView!!.addView(nutritionAdapter.getView(position, null, null))
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