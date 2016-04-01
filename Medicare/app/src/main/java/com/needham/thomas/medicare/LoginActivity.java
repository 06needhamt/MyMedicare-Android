package com.needham.thomas.medicare;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LoginActivity extends FragmentActivity {

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
}
