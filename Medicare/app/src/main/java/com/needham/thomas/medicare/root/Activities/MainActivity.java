package com.needham.thomas.medicare.root.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.needham.thomas.medicare.R;

/**
 * Created by Thomas Needham on 01/04/2016.
 */
public class MainActivity extends FragmentActivity {

    private int width;
    private int height;
    private DisplayMetrics dm;
    private TextView title;
    private Button btnRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();

    }

    /**
     * This function sets up the user interface
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.welcome);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        SetupTitleLayout();
        SetupLoginButtonLayout();
        SetupRegisterButtonLayout();
        SetupLoginOnClick();
        SetupRegisterOnClick();
    }

    private void SetupRegisterOnClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Register Clicked");
                Intent i = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void SetupLoginOnClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Login Clicked");
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * This function sets up layout for the register button
     */
    private void SetupRegisterButtonLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.50);
        btnRegister.setGravity(Gravity.CENTER);
        btnRegister.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the login button
     */
    private void SetupLoginButtonLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        btnLogin.setGravity(Gravity.CENTER);
        btnLogin.setLayoutParams(params);
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
