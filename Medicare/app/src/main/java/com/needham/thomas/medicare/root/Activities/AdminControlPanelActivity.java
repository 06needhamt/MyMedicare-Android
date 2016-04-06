package com.needham.thomas.medicare.root.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;


import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AdminControlPanelActivity extends FragmentActivity implements IAppConstants{

    int width;
    int height;
    DisplayMetrics dm;
    TextView title;
    Button btnViewAllRecords;
    Button btnResetPassword;
    Button btnViewAllUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_panel);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    /**
     * This function sets up the user interface for AdminControlPanelActivity
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.adminControlTitle);
        btnViewAllRecords = (Button) findViewById(R.id.btnViewAllRecords);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        btnViewAllUsers = (Button) findViewById(R.id.btnViewAllUsers);
        SetupTitleLayout();
        SetupViewAllRecordsLayout();
        SetupResetPasswordLayout();
        SetupViewAllUsersLayout();
        SetupViewAllRecordsOnClick();
        SetupResetPasswordOnClick();
        SetupViewAllUsersOnClick();
        ApplyUserSettings();
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
                    btnViewAllRecords.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnViewAllUsers.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                }
                if (u.getBackgroundColour() != 0) {
                    FrameLayout layout = (FrameLayout) findViewById(R.id.adminControlRoot);
                    layout.setBackgroundColor(getResources().getColor(u.getBackgroundColour()));
                }
                if (u.getFontColour() != 0) {
                    title.setTextColor(getResources().getColor(u.getFontColour()));
                    btnViewAllRecords.setTextColor(getResources().getColor(u.getFontColour()));
                    btnViewAllUsers.setTextColor(getResources().getColor(u.getFontColour()));
                    btnResetPassword.setTextColor(getResources().getColor(u.getFontColour()));
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
     * On click listener for the view all users button
     */
    private void SetupViewAllUsersOnClick() {
        btnViewAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Admin", "View Users Button Clicked");
                Intent i = new Intent(getBaseContext(), ViewUsersActivity.class);
                startActivity(i);
            }
        });
    }
    /**
     * On click listener for the reset button
     */
    private void SetupResetPasswordOnClick() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Admin", "Reset Password button Clicked");
                Intent i = new Intent(getBaseContext(), ResetPasswordActivity.class);
                startActivity(i);
            }
        });
    }
    /**
     * On click listener for the view all records button
     */
    private void SetupViewAllRecordsOnClick() {
        btnViewAllRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Admin", "View All Records Button Clicked");
                Bundle b = new Bundle();
                b.putString(FROM_KEY, "AdminControlPanelActivity");
                b.putString(USER_NAME_KEY, "admin");
                Intent i = new Intent(getBaseContext(),ViewRecordActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
    /**
     * Sets up the layout for the view all users button
     */
    private void SetupViewAllUsersLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.45);
        btnViewAllUsers.setGravity(Gravity.CENTER);
        btnViewAllUsers.setLayoutParams(params);
    }
    /**
     * Sets up the layout for the reset password button
     */
    private void SetupResetPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        btnResetPassword.setGravity(Gravity.CENTER);
        btnResetPassword.setLayoutParams(params);
    }
    /**
     * Sets up the layout for the view all records button
     */
    private void SetupViewAllRecordsLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.15);
        btnViewAllRecords.setGravity(Gravity.CENTER);
        btnViewAllRecords.setLayoutParams(params);
    }
    /**
     * Sets up the layout for the title text view
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
}
