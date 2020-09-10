package com.adisa.diningplus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.adisa.diningplus.adapters.FollowDialogAdapter;

import java.util.HashSet;

/**
 * Created by Adisa on 5/4/2017.
 */

public class FollowDialogPreference extends DialogPreference {
    public FollowDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.follow_dialog);
    }

    @Override
    public void onBindDialogView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.followList);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        HashSet<String> stringSet = (HashSet<String>) preferences.getStringSet("followedItems", new HashSet<String>());
        FollowDialogAdapter followDialogAdapter = new FollowDialogAdapter(getContext(), stringSet);
        listView.setAdapter(followDialogAdapter);

        super.onBindDialogView(view);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        persistBoolean(positiveResult);
    }
}
