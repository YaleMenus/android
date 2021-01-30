package com.adisa.diningplus.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import com.adisa.diningplus.R
import com.adisa.diningplus.adapters.MenuAdapter
import com.adisa.diningplus.network.entities.Item
import com.adisa.diningplus.network.entities.Meal
import com.adisa.diningplus.network.API
import com.adisa.diningplus.utils.DateFormatProvider
import com.google.android.material.appbar.CollapsingToolbarLayout
import java.util.*

class HallActivity : AppCompatActivity(), OnDateSetListener {
    var api: API? = null
    var collapsingToolbar: CollapsingToolbarLayout? = null
    var hallName: String? = null
    var hallId: String? = null
    var mealItems: HashMap<String, List<Item>>? = null
    var headerMap = HashMap<String, Int>()
    var meals: List<Meal>? = null
    var menuAdapter: MenuAdapter? = null
    var expandableListView: ExpandableListView? = null
    var coordinatorLayout: CoordinatorLayout? = null
    var preferences: SharedPreferences? = null
    var emptyView: View? = null
    var loadingView: View? = null
    var date: Calendar? = null
    var dateButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hall)
        api = API(this)
        val toolbar = findViewById<View>(R.id.anim_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        collapsingToolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        coordinatorLayout = findViewById<View>(R.id.coord_layout) as CoordinatorLayout
        headerMap["BK"] = R.drawable.berkeley_header
        headerMap["BR"] = R.drawable.branford_header
        headerMap["GH"] = R.drawable.hopper_header
        headerMap["ES"] = R.drawable.stiles_header
        headerMap["DC"] = R.drawable.davenport_header
        headerMap["BF"] = R.drawable.franklin_header
        headerMap["MY"] = R.drawable.murray_header
        headerMap["JE"] = R.drawable.je_header
        headerMap["MC"] = R.drawable.morse_header
        headerMap["PC"] = R.drawable.pierson_header
        headerMap["SY"] = R.drawable.saybrook_header
        headerMap["SM"] = R.drawable.silliman_header
        headerMap["TD"] = R.drawable.td_header
        headerMap["TC"] = R.drawable.trumbull_header
        expandableListView = findViewById<View>(R.id.expandableListView) as ExpandableListView
        expandableListView!!.setChildDivider(resources.getDrawable(R.color.transparent))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandableListView!!.isNestedScrollingEnabled = true
        } else {
            val params = coordinatorLayout!!.layoutParams as CoordinatorLayout.LayoutParams
            val tv = TypedValue()
            theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
            params.bottomMargin = resources.getDimensionPixelSize(tv.resourceId)
            coordinatorLayout!!.layoutParams = params
        }
        expandableListView!!.setGroupIndicator(null)
        expandableListView!!.setOnChildClickListener { expandableListView, view, mealPosition, itemPosition, id ->
            val item = menuAdapter!!.getChild(mealPosition, itemPosition) as Item
            val i = Intent()
            i.setClass(applicationContext, ItemActivity::class.java)
            i.putExtra("name", item.name)
            i.putExtra("id", item.id)
            startActivity(i)
            true
        }
        val i = intent
        hallName = i.getStringExtra("name")
        hallId = i.getStringExtra("id")
        collapsingToolbar!!.title = hallName
        val header = findViewById<View>(R.id.header) as ImageView
        header.setImageDrawable(resources.getDrawable(headerMap.get(hallId!!) ?: R.drawable.berkeley_header))
        emptyView = findViewById(R.id.hall_empty)
        loadingView = findViewById(R.id.loader)
        expandableListView!!.emptyView = emptyView
        dateButton = findViewById(R.id.dateButton)
        date = Calendar.getInstance()
        getMeals()
    }

    private fun getMeals() {
        dateButton!!.text = DateFormatProvider.dateFull.format(date!!.time)
        val mealsTask = MealsTask(date)
        mealsTask.execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_hall, menu)
        if (preferences!!.getString("default_hall_id", "") === hallId) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_black_24dp)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                val editor = preferences!!.edit()
                if (preferences!!.getString("default_hall_id", "") !== hallId) {
                    item.setIcon(R.drawable.ic_favorite_black_24dp)
                    editor.putString("default_hall_id", hallId)
                    editor.putString("default_hall_name", hallName)
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp)
                    editor.putString("default_hall_id", "")
                }
                editor.apply()
                true
            }
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)
                upIntent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this) // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent) // Navigate up to the closest parent
                            .startActivities()
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent)
                }
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun previousDate(view: View?) {
        date!!.add(Calendar.DATE, -1)
        getMeals()
    }

    fun nextDate(view: View?) {
        date!!.add(Calendar.DATE, 1)
        getMeals()
    }

    fun chooseDate(view: View?) {
        val datePickerDialog = DatePickerDialog(
                this,
                this,
                date!![Calendar.YEAR],
                date!![Calendar.MONTH],
                date!![Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        date!![Calendar.YEAR] = year
        date!![Calendar.MONTH] = month
        date!![Calendar.DAY_OF_MONTH] = dayOfMonth
        getMeals()
    }

    private inner class MealsTask(var date: Calendar?) : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("get", "start")
            expandableListView!!.emptyView = findViewById(R.id.loader)
            emptyView!!.visibility = View.GONE
        }

        override fun doInBackground(vararg params: Void?): Void? {
//            try {
            meals = api!!.getHallMeals(hallId!!, this.date!!)
            //            } catch (JSONException | IOException e) {
//                Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
            mealItems = HashMap()
            for (meal in meals!!) {
                var items: List<Item>
                //                try {
                items = api!!.getMealItems(meal.id)
                //                } catch (JSONException | IOException e) {
//                    Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
                mealItems!![meal.name] = items
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            menuAdapter = MenuAdapter(this@HallActivity, meals!!, mealItems!!)
            expandableListView!!.setAdapter(menuAdapter)
            if (menuAdapter!!.groupCount > 0) expandableListView!!.expandGroup(0)
            expandableListView!!.emptyView = findViewById(R.id.hall_empty)
            val dietaryRestrictionTask = DietaryRestrictionTask()
            dietaryRestrictionTask.execute()
            Log.d("get", "done")
        }
    }

    private inner class DietaryRestrictionTask : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val dietaryRestrictions = preferences!!.getStringSet("dietary_restrictions", HashSet()) as HashSet<String>?
            for (meal in meals!!) {
                val items = mealItems!![meal.name]
                if (items != null) {
                    for (item in items) {
                        for (restriction in dietaryRestrictions!!) {
                            try {
                                if (item.javaClass.getField(restriction)[item] as Boolean) {
                                    item.restricted = true
                                    break
                                }
                            } catch (e: NoSuchFieldException) {
                                // Should never happen
                            } catch (e: IllegalAccessException) {
                                // Should never happen
                            }
                        }
                    }
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            menuAdapter!!.updateItems(mealItems!!)
            loadingView!!.visibility = View.GONE
        }
    }
}