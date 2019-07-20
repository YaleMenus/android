package com.adisa.diningplus;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Adisa on 4/30/2017.
 */

public class TraitDialogFragment extends android.support.v4.app.DialogFragment {

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
        String[] traitArray = getResources().getStringArray(R.array.traits);
        boolean[] selections = new boolean[traitArray.length];
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        HashSet<String> currTraits = (HashSet<String>) preferences.getStringSet("traitPrefs", new HashSet<String>());

        for (int i = 0; i < traitArray.length; i++) {
            if (currTraits.contains(traitArray[i])) {
                selections[i] = true;
            } else {
                selections[i] = false;
            }
        }

        final boolean[] finalSelections = selections;
        builder.setTitle("Select dietary traits that you want to avoid.")
                .setMultiChoiceItems(R.array.traits, selections,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    finalSelections[which] = true;
                                } else {
                                    finalSelections[which] = false;
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = preferences.edit();
                        HashSet<String> traitSet = new HashSet<String>();
                        String[] traitArray = getResources().getStringArray(R.array.traits);
                        for (int i = 0; i < finalSelections.length; i++) {
                            if (finalSelections[i]) {
                                traitSet.add(traitArray[i]);
                            }
                        }
                        editor.putStringSet("traitPrefs", traitSet);
                        editor.apply();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
