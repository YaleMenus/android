package com.adisa.diningplus.fragments

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.adisa.diningplus.R
import java.util.*

class DietaryRestrictionDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        val restrictionOptions = resources.getStringArray(R.array.dietary_restrictions)
        val selections = BooleanArray(restrictionOptions.size)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val allergens = preferences.getStringSet("dietary_restrictions", HashSet()) as HashSet<String>?
        for (i in restrictionOptions.indices) {
            selections[i] = allergens!!.contains(restrictionOptions[i])
        }
        builder.setTitle("Select your dietary restrictions.")
                .setMultiChoiceItems(R.array.dietary_restriction_labels, selections) { dialog, which, isChecked -> selections[which] = isChecked }
                .setPositiveButton("OK") { dialog, id ->
                    val editor = preferences.edit()
                    val traitSet = HashSet<String>()
                    val traitArray = resources.getStringArray(R.array.dietary_restrictions)
                    for (i in selections.indices) {
                        if (selections[i]) {
                            traitSet.add(traitArray[i])
                        }
                    }
                    editor.putStringSet("dietary_restrictions", traitSet)
                    editor.apply()
                }
        // Create the AlertDialog object and return it
        return builder.create()
    }
}