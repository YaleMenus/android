package com.adisa.diningplus

import android.app.AlertDialog
import android.content.Context
import android.preference.DialogPreference
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.View
import android.widget.ListView
import com.adisa.diningplus.adapters.FollowDialogAdapter
import java.util.*

class FollowDialogPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {
    public override fun onBindDialogView(view: View) {
        val listView = view.findViewById<View>(R.id.followList) as ListView
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val stringSet = preferences.getStringSet("followedItems", HashSet()) as HashSet<String>?
        val followDialogAdapter = FollowDialogAdapter(context, stringSet)
        listView.adapter = followDialogAdapter
        super.onBindDialogView(view)
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        builder.setNegativeButton(null, null)
        super.onPrepareDialogBuilder(builder)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)
        persistBoolean(positiveResult)
    }

    init {
        dialogLayoutResource = R.layout.follow_dialog
    }
}