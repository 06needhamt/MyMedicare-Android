package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Thomas Needham on 01/04/2016.
 */
public class LoginActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant {

    private int width;
    private int height;
    private TextView title;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLoginSubmit;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    /**
     * This function sets up the login activity user interface
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.login_title);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLoginSubmit = (Button) findViewById(R.id.btnLoginSubmit);
        SetupTitleLayout();
        SetupUsernameLayout();
        SetupPasswordLayout();
        SetupSubmitLayout();
        SetupSubmitOnClickListener();

    }

    /**
     * On click listener for the submit button
     */
    private void SetupSubmitOnClickListener() {
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Login Submit button clicked");
                if (!CheckUserCredentials()) {
                    Log.e("Login", "Invalid Credentials");
                    InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("The username or password is incorrect");
                    fragment.show(getFragmentManager(), "dia");
                    return;
                }
                Log.e("Login", "User " + txtUsername.getText().toString() + " Successfully logged in");
                Toast.makeText(getBaseContext(), "User " + txtUsername.getText().toString()
                        + " Successfully logged in",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This function checks the credentials inputted by the user
     * @return whether the credentials are correct
     */
    private boolean CheckUserCredentials() {
        UserDetails details = ReadUsers();
        if(details == null){
            Log.e("Login", "An error occurred while reading the users from the file");
            InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("no user file found",this);
            fragment.show(getFragmentManager(), "dia");
            return false;
        }
        String userName = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if(userName.equals("") || password.equals("")){
            Log.e("Login", "Invalid Credentials");
            InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("The username or password is incorrect",this);
            fragment.show(getFragmentManager(),"dia");
            return false;
        }
        assert details != null;
        for(User u : details.getUserinfo()){
            if(u.getUserName().equals(userName) && u.getPassword().equals(password))
                return true;
        }
        return false;
    }

    /**
     * This function reads the list of users from the file
     * @return the list of users or null if an error occurred
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
     * This function sets up the submit button layout
     */
    private void SetupSubmitLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnLoginSubmit.setGravity(Gravity.CENTER);
        btnLoginSubmit.setLayoutParams(params);
    }

    /**
     * This function sets up layout for the password edit text
     */
    private void SetupPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        txtPassword.setGravity(Gravity.CENTER);
        txtPassword.setLayoutParams(params);
    }

    /**
     * This function sets up layout for the username edit text
     */
    private void SetupUsernameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
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
     * @param pin the pin entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setPositiveButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doYesConfirmClick(String pin) {

    }

    /**
     * This method is called when the negative button is pressed on the dialog Fragment
     *
     * @param pin the pin entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setNegativeButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doNoConfirmClick(String pin) {

    }
}
