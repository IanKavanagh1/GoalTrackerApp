package com.example.goal_tracker

import account_creation.AccountManager
import android.app.Activity
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import shared.EditableFieldType

class EditTextDialog(activity: Activity, userId: Int)
{
    // store passed in activity
    private val act = activity
    private val id = userId

    // variable for the alert dialog
    private lateinit var alertDialog: AlertDialog

    private lateinit var editText: EditText
    private lateinit var confirmButton: Button

    // public method to start the loading dialog
    fun startDialog(type: EditableFieldType)
    {
        // initialise the dialog
        val builder = AlertDialog.Builder(act)
        val inflater = act.layoutInflater

        // set up the dialog layout
        builder.setView(inflater.inflate(R.layout.custom_editor_pop_up, null))

        //allow the user to cancel the dialog if tapped
        builder.setCancelable(true)

        // create and show the dialog
        alertDialog = builder.create()
        alertDialog.show()

        editText = alertDialog.findViewById(R.id.editTextPopUp)

        confirmButton = alertDialog.findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener { confirmChanges(type) }
    }

    private fun dismissDialog()
    {
        // close the dialog
        alertDialog.dismiss()
    }

    private fun confirmChanges(type: EditableFieldType)
    {
        // Switch statement to check what type of field we should edit
        when(type)
        {
            EditableFieldType.Email -> {
                if (AccountManager.updateEmail(id, editText.text.toString()))
                {
                    // Display successful update notification
                    Toast.makeText(act, "Email Updated Successfully", Toast.LENGTH_SHORT).show()
                    dismissDialog()
                }
                else
                {
                    // Display failed update notification
                    Toast.makeText(act, "Failed To Update Email", Toast.LENGTH_SHORT).show()
                }
            }
            EditableFieldType.DisplayName -> {
                if(AccountManager.updateDisplayName(id, editText.text.toString()))
                {
                    // Display successful update notification
                    Toast.makeText(act, "Display Name Updated Successfully", Toast.LENGTH_SHORT).show()
                    dismissDialog()
                }
                else
                {
                    // Display failed update notification
                    Toast.makeText(act, "Failed To Update Display Name", Toast.LENGTH_SHORT).show()
                }
            }
            EditableFieldType.Password -> {
                if(AccountManager.updatePassword(id, editText.text.toString()))
                {
                    // Display successful update notification
                    Toast.makeText(act, "Password Updated Successfully", Toast.LENGTH_SHORT).show()
                    dismissDialog()
                }
                else
                {
                    // Display failed update notification
                    Toast.makeText(act, "Failed To Update Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}