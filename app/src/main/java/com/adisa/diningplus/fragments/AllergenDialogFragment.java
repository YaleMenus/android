package com.adisa.diningplus.fragments;

import android.app.Activity;
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

public class AllergenDialogFragment extends DialogFragment {

    private String cleanTrait(String trait) {
        trait = trait.toLowerCase().replace(' ', '_');
        // Remove parenthesized explanation if present
        int parenthesisIndex = trait.indexOf('(');
        if (parenthesisIndex != -1) {
            trait = trait.substring(0, parenthesisIndex - 1);
        }
        return trait;
    }

    // Use this instance of the interface to deliver action events
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] allergenOptions = getResources().getStringArray(R.array.allergens);
        boolean[] selections = new boolean[allergenOptions.length];
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        HashSet<String> allergens = (HashSet<String>) preferences.getStringSet("allergens", new HashSet<String>());

        for (int i = 0; i < allergenOptions.length; i++) {
            selections[i] = allergens.contains(cleanTrait(allergenOptions[i]));
        }

        final boolean[] finalSelections = selections;
        builder.setTitle("Choose your dietary restrictions, or other ingredients you want to be warned about.")
                .setMultiChoiceItems(R.array.allergens, selections, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        finalSelections[which] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = preferences.edit();
                        HashSet<String> traitSet = new HashSet<String>();
                        String[] traitArray = getResources().getStringArray(R.array.allergens);
                        for (int i = 0; i < finalSelections.length; i++) {
                            if (finalSelections[i]) {
                                traitSet.add(cleanTrait(traitArray[i]));
                                System.out.println(cleanTrait(traitArray[i]));
                            }
                        }
                        editor.putStringSet("allergens", traitSet);
                        editor.apply();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
