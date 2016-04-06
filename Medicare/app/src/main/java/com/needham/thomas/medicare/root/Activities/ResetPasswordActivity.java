package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;
import com.needham.thomas.medicare.root.dialogs.IConfirmInputDialogCompliant;
import com.needham.thomas.medicare.root.dialogs.InvalidInputDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ResetPasswordActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant {

    int width;
    int height;
    DisplayMetrics dm;
    TextView title;
    EditText txtUsername;
    EditText txtPassword;
    EditText txtReEnterPassword;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    /**
     * This function sets up the user interface for the reset password button
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.ResetPasswordTitle);
        txtUsername = (EditText) findViewById(R.id.txtResetPasswordUsername);
        txtPassword = (EditText) findViewById(R.id.txtresetPasswordPW);
        txtReEnterPassword = (EditText) findViewById(R.id.txtResetPasswordReenterPW);
        btnSubmit = (Button) findViewById(R.id.btnResetPasswordSubmit);
        SetupTitleLayout();
        SetupUsernameLayout();
        SetupPasswordLayout();
        SetupReEnterPasswordLayout();
        SetupSubmitLayout();
        SetupSubmitOnClick();
        ApplyUserSettings();
    }

    /**
     * This function writes user details to the user file
     * @param details the details for write
     * @return whether the operation completed successfully
     */
    private boolean WriteUserDetails(UserDetails details) {
        try {
            File file = new File(getFilesDir().getAbsolutePath(), USER_FILE_NAME);
            Log.e("File Size", String.valueOf(file.length()));
            Log.e("List Size ", String.valueOf(details.getUserinfo().size()));
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(details);
            Log.e("File Size", String.valueOf(file.length()));
        }
        catch (IOException ex){
            ex.printStackTrace();
            Log.e("WriteUser", "An error occurred while writing the user to a file");
            throw new Error("\"An error occurred while writing the user to a file\"");
        }
        return true;
    }
    /**
     * This function applies the current user's chosen customisation settings
     */
    private void ApplyUserSettings() {
        UserDetails users = ReadUsers();
        assert users != null;
        assert users.getUserinfo() != null;
        for (User u : users.getUserinfo()) {
            if (u.getUserName().equals("admin")) {
                if (u.getFontSize() != 0.0f) {
                    btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                }
                if (u.getBackgroundColour() != 0) {
                    FrameLayout layout = (FrameLayout) findViewById(R.id.resetPasswordRoot);
                    layout.setBackgroundColor(getResources().getColor(u.getBackgroundColour()));
                }
                if (u.getFontColour() != 0) {
                    title.setTextColor(getResources().getColor(u.getFontColour()));
                    btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                }
                break;
            }
        }
    }

    /**
     * This function reads all currently registered users from the user file
     * @return an object containing a list of all users
     */
    private UserDetails ReadUsers() {
        try {
            File file = new File(getFilesDir(), USER_FILE_NAME);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (UserDetails) objectInputStream.readObject();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            Log.e("ReadAppointment", "An error occurred while reading the appointment from the file");
        }
        catch (ClassNotFoundException cls)
        {
            cls.printStackTrace();
            Log.e("ReadAppointment", "Class Not Found");
            throw new Error("Class Not Found");
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }
        return null;
    }

    /**
     * On Click listener for the submit button
     */
    private void SetupSubmitOnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Reset Password", "Reset password submit button clicked");
                if(!ResetPassword()){
                    Log.e("Reset Password", "An error occurred while resetting password");
                }
            }
        });
    }

    /**
     * this function resets a users password
     * @return whether the operation completed successfully
     */
    private boolean ResetPassword() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String reEnteredPassword = txtReEnterPassword.getText().toString();
        UserDetails userDetails = ReadUsers();
        assert userDetails != null;
        for(User u : userDetails.getUserinfo()){
            if(u.getUserName().equals(username)){
                if(!password.equals(reEnteredPassword) || password.length() == 0 || reEnteredPassword.length() == 0){
                    InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("Passwords do not match", this);
                    fragment.show(getFragmentManager(),"dia");
                    return false;
                }
                u.setPassword(password);
                WriteUserDetails(userDetails);
                Toast.makeText(getBaseContext(), "Password successfully changed", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        return true;
    }

    /**
     * This function sets up the layout for the submit button
     */
    private void SetupSubmitLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.70);
        btnSubmit.setGravity(Gravity.CENTER);
        btnSubmit.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the re-enter password edit text
     */
    private void SetupReEnterPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.35);
        txtReEnterPassword.setGravity(Gravity.CENTER);
        txtReEnterPassword.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the password edit text
     */
    private void SetupPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.25);
        txtPassword.setGravity(Gravity.CENTER);
        txtPassword.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the username edit text
     */
    private void SetupUsernameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.15);
        txtUsername.setGravity(Gravity.CENTER);
        txtUsername.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the title text view
     */
    private void SetupTitleLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.01);
        title.setGravity(Gravity.CENTER);
        title.setLayoutParams(params);
    }

    /**
     * gets the screen width and size for dynamic creation of the ui
     * @param dm the display metrics object to store the values in
     */
    protected void GetDisplayMetrics(DisplayMetrics dm) {
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    /**
     * This method is called when the positive button is pressed on the dialog Fragment
     *
     * @param input the input entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setPositiveButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doYesConfirmClick(String input) {

    }

    /**
     * This method is called when the negative button is pressed on the dialog Fragment
     *
     * @param input the input entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setNegativeButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doNoConfirmClick(String input) {

    }
}
