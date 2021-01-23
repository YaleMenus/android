package com.adisa.diningplus.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.adisa.diningplus.R;

import java.util.HashSet;

/**
 * Created by Adisa on 4/30/2017.
 */

public class DietaryRestrictionDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] restrictionOptions = getResources().getStringArray(R.array.dietary_restrictions);
        boolean[] selections = new boolean[restrictionOptions.length];
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        HashSet<String> allergens = (HashSet<String>) preferences.getStringSet("dietary_restrictions", new HashSet<String>());

        for (int i = 0; i < restrictionOptions.length; i++) {
            selections[i] = allergens.contains(restrictionOptions[i]);
        }

        final boolean[] finalSelections = selections;
        builder.setTitle("Select your dietary restrictions.")
                .setMultiChoiceItems(R.array.dietary_restriction_labels, selections, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        finalSelections[which] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = preferences.edit();
                        HashSet<String> traitSet = new HashSet<String>();
                        String[] traitArray = getResources().getStringArray(R.array.dietary_restrictions);
                        for (int i = 0; i < finalSelections.length; i++) {
                            if (finalSelections[i]) {
                                traitSet.add(traitArray[i]);
                            }
                        }
                        editor.putStringSet("dietary_restrictions", traitSet);
                        editor.apply();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
