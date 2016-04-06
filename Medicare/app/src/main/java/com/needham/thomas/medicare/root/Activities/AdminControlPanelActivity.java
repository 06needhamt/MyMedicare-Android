package com.needham.thomas.medicare.root.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;


import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;

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
    }

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

    private void SetupViewAllUsersLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.45);
        btnViewAllUsers.setGravity(Gravity.CENTER);
        btnViewAllUsers.setLayoutParams(params);
    }

    private void SetupResetPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        btnResetPassword.setGravity(Gravity.CENTER);
        btnResetPassword.setLayoutParams(params);
    }

    private void SetupViewAllRecordsLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.15);
        btnViewAllRecords.setGravity(Gravity.CENTER);
        btnViewAllRecords.setLayoutParams(params);
    }

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
