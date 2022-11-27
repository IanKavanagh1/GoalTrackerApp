package com.example.goal_tracker

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(activity: Activity)
{
    private val act = activity
    private lateinit var alertDialog: AlertDialog

    fun startLoadingDialog()
    {
        val builder = AlertDialog.Builder(act)
        val inflater = act.layoutInflater

        builder.setView(inflater.inflate(R.layout.custon_loading_dialog, null))
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismissDialog()
    {
        alertDialog.dismiss()
    }
}