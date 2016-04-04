package com.needham.thomas.medicare.root.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.needham.thomas.medicare.R;

/**
 * Created by thoma on 01/04/2016.
 */
public class DeleteRecordConfirmDialogFragment extends DialogFragment implements DialogInterface {
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
    public DeleteRecordConfirmDialogFragment() {
    }

    /**
     * Required constructor definition
     */
    public DeleteRecordConfirmDialogFragment(String Message) {
        this.Message = Message;
    }

    /**
     * Required constructor definition DO NOT USE
     */
    @Deprecated
    private DeleteRecordConfirmDialogFragment(IConfirmInputDialogCompliant caller) {
        this.caller = caller;
    }

    /**
     * Default constructor for this dialog fragment
     *
     * @param message The message to be shown on the dialog fragment
     * @param caller  A reference to {@link IConfirmInputDialogCompliant}
     *                where all handled evrnts will be sent to
     */
    public DeleteRecordConfirmDialogFragment(String message, IConfirmInputDialogCompliant caller) {
        this.Message = message;
        this.caller = caller;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    caller.doNoConfirmClick("");
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
