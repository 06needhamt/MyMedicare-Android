package com.needham.thomas.medicare.root.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.needham.thomas.medicare.R;


/**
 * Created by Tom on 01/04/2016.
 * This Class creates and displays a {@link android.app.DialogFragment} which allows the user to enter
 * their 4 digit pin code
 */
public class PasswordDialogFragment extends DialogFragment implements DialogInterface{
    /**
     * The textview object that will be used to input the pin number entered by the user
     */
    TextView input;
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
    public PasswordDialogFragment() { }
    /**
     * Required constructor definition DO NOT USE
     */
    @Deprecated
    private PasswordDialogFragment(String Message) {this.Message = Message;}
    /**
     * Required constructor definition DO NOT USE
     */
    @Deprecated
    private PasswordDialogFragment(IConfirmInputDialogCompliant caller) {this.caller = caller;}

    /**
     * Default constructor for this dialog fragment
     * @param message The message to be shown on the dialog fragment
     * @param caller A reference to {@link IConfirmInputDialogCompliant}
     *               where all handled evrnts will be sent to
     */
    public PasswordDialogFragment(String message, IConfirmInputDialogCompliant caller) {this.Message = message; this.caller = caller;}

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        input = new EditText(getActivity());
        input.setTextColor(getActivity().getResources().getColor(R.color.Black));
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Message);
        builder.setView(input);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    String temp = input.getText().toString();
                    caller.doYesConfirmClick(temp);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    String temp = "";
                    caller.doNoConfirmClick(temp);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    cancel();
                    String temp = input.getText().toString();
                    caller.doNoConfirmClick(temp);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return builder.create();
    }

    /**
     * This method is called when the cancel button is pressed in the dialog fragment
     */
    @Override

    public void cancel() {
        input.setText("cancel");
    }
}
