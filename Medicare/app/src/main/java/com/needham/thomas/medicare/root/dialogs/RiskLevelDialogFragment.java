package com.needham.thomas.medicare.root.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by 22416714 on 11/04/2016.
 */
public class RiskLevelDialogFragment extends DialogFragment implements DialogInterface {

    /**
     * The message to be displayed on the dialog fragment
     */
    String Message;
    /**
     * A Reference to a class which implements {@link IConfirmInputDialogCompliant}
     * which will be used to handle all events from this dialog fragment
     */
    IConfirmInputDialogCompliant caller;

    /**
     * Required constructor definition DO NOT USE
     */
    @Deprecated
    public RiskLevelDialogFragment() {
    }

    /**
     * Required constructor definition
     */
    public RiskLevelDialogFragment(String Message) {
        this.Message = Message;
    }

    /**
     * Required constructor definition DO NOT USE
     */
    @Deprecated
    private RiskLevelDialogFragment(IConfirmInputDialogCompliant caller) {
        this.caller = caller;
    }

    /**
     * Default constructor for this dialog fragment
     *
     * @param message The message to be shown on the dialog fragment
     * @param caller  A reference to {@link IConfirmInputDialogCompliant}
     *                where all handled evrnts will be sent to
     */
    public RiskLevelDialogFragment(String message, IConfirmInputDialogCompliant caller) {
        this.Message = message;
        this.caller = caller;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    caller.doYesConfirmClick("");
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

        });
        return builder.show();
    }
    @Override
    public void cancel() {
        
    }
}
