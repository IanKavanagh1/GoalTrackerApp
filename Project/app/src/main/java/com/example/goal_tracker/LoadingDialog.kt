package com.example.goal_tracker

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(activity: Activity)
{
    // store passed in activity
    private val act = activity
    // variable for the alert dialog
    private lateinit var alertDialog: AlertDialog

    // public method to start the loading dialog
    fun startLoadingDialog()
    {
        // initialise the dialog
        val builder = AlertDialog.Builder(act)
        val inflater = act.layoutInflater

        // set up the dialog layout
        builder.setView(inflater.inflate(R.layout.custon_loading_dialog, null))
        // don't allow the user to cancel the dialog if tapped
        builder.setCancelable(false)

        // create and show the dialog
        alertDialog = builder.create()
        alertDialog.show()
    }

    // public method to close the dialog
    fun dismissDialog()
    {
        // close the dialog
        alertDialog.dismiss()
    }
}