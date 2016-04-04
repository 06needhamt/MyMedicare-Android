package com.needham.thomas.medicare.root.dialogs;

/**
 * Created by thoma on 01/04/2016.
 */
public interface IConfirmInputDialogCompliant {
    /**
     * This method is called when the positive button is pressed on the dialog Fragment
     * @see android.app.AlertDialog.Builder#setPositiveButton(CharSequence, android.content.DialogInterface.OnClickListener)
     * @param input the input entered into the dialog fragment by the user
     */
    public void doYesConfirmClick(String input);

    /**
     * This method is called when the negative button is pressed on the dialog Fragment
     * @see android.app.AlertDialog.Builder#setNegativeButton(CharSequence, android.content.DialogInterface.OnClickListener)
     * @param input the input entered into the dialog fragment by the user
     *
     */
    public void doNoConfirmClick(String input);
}
