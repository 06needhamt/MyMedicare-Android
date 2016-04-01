package com.needham.thomas.medicare.root.Activities;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.needham.thomas.medicare.R;

public class RegisterActivity extends FragmentActivity {

    private int width;
    private int height;
    private TextView title;
    private EditText txtfirstName;
    private EditText txtlastName;
    private EditText txtage;
    private EditText txtaddress1;
    private EditText txtaddress2;
    private EditText txtphoneNumber;
    private EditText txtnurseName;
    private Button btnSubmit;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    /**
     * This function sets up the register activity user interface
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.register_title);
        txtfirstName = (EditText) findViewById(R.id.txtRegisterFirstName);
        txtlastName = (EditText) findViewById(R.id.txtRegisterLastName);
        txtage = (EditText) findViewById(R.id.txtRegisterAge);
        txtaddress1 = (EditText) findViewById(R.id.txtRegisterAddress1);
        txtaddress2 = (EditText) findViewById(R.id.txtRegisterAddress2);
        txtphoneNumber = (EditText) findViewById(R.id.txtRegisterPhoneNumber);
        txtnurseName = (EditText) findViewById(R.id.txtRegisterNurseName);
        btnSubmit = (Button) findViewById(R.id.btnRegisterSubmit);
        SetupTitleLayout();
        SetupFirstNameLayout();
        SetupLastNameLayout();
        SetupAgeLayout();
        SetupAddress1Layout();
        SetupAddress2Layout();
        SetupPhoneNumberLayout();
        SetupNurseNameLayout();
        SetupSubmitLayout();

    }

    /**
     * This function sets up the layout for the submit button
     */
    private void SetupSubmitLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.80);
        btnSubmit.setGravity(Gravity.CENTER);
        btnSubmit.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the nurse name edit text
     */
    private void SetupNurseNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.70);
        txtnurseName.setGravity(Gravity.CENTER);
        txtnurseName.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the phone number edit text
     */
    private void SetupPhoneNumberLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        txtphoneNumber.setGravity(Gravity.CENTER);
        txtphoneNumber.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the address 2 edit text
     */
    private void SetupAddress2Layout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.50);
        txtaddress2.setGravity(Gravity.CENTER);
        txtaddress2.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the address 1 edit text
     */
    private void SetupAddress1Layout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        txtaddress1.setGravity(Gravity.CENTER);
        txtaddress1.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the age edit text
     */
    private void SetupAgeLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        txtage.setGravity(Gravity.CENTER);
        txtage.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the last name edit text
     */
    private void SetupLastNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.20);
        txtlastName.setGravity(Gravity.CENTER);
        txtlastName.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the first name edit text
     */
    private void SetupFirstNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.10);
        txtfirstName.setGravity(Gravity.CENTER);
        txtfirstName.setLayoutParams(params);
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
