package com.needham.thomas.medicare.root.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
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

public class HomeActivity extends FragmentActivity implements IAppConstants {

    int width;
    int height;
    DisplayMetrics dm;
    TextView title;
    Button btnNewRecord;
    Button btnViewRecords;
    Button btnCustomize;
    Button btnAdminControl;
    Button btnLogout;

    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        UnpackBundle();
        SetupViews();

    }

    /**
     * This function unpacks the bundle containing the username of the currently logged in user
     */
    private void UnpackBundle() {
        Bundle b = getIntent().getExtras();
        if(b == null)
            throw new Error("null bundle found");
        if(b.getString(FROM_KEY,"").equals("LoginActivity")){
            currentUser = b.getString(USER_NAME_KEY,"");
            if(currentUser.equals(""))
                throw new Error("Invalid user logged in");
        }
        else
            throw new Error("Invalid bundle Found");
        // password and other details will be needed later
    }

    /**
     * this function sets up the user interface for home activity
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.menu_title);
        btnNewRecord = (Button) findViewById(R.id.btnNewReading);
        btnViewRecords = (Button) findViewById(R.id.btn_ViewReadings);
        btnCustomize = (Button) findViewById(R.id.btnCustomise);
        btnAdminControl = (Button) findViewById(R.id.btnAdminControl);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        SetupTitleLayout();
        SetupNewRecordLayout();
        SetupViewRecordLayout();
        SetupCustomizeLayout();
        SetupAdminControlLayout();
        SetupNewRecordOnClick();
        SetupViewRecordOnClick();
        SetupCutomiseOnClick();
        SetupAdminControlOnClick();
        SetupLogoutLayout();
        SetupLogoutOnClick();
    }

    private void SetupLogoutOnClick() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("logout", "Logout button clicked");
                currentUser = "";
                finish();
                Toast.makeText(getBaseContext(), "User Successfully logged out", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetupLogoutLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.75);
        btnLogout.setGravity(Gravity.CENTER);
        btnLogout.setLayoutParams(params);
    }

    /**
     * On click listener for the admin control button
     */
    private void SetupAdminControlOnClick() {
        btnAdminControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "Admin Control Button Clicked");
            }
        });
    }

    /**
     * On click listener for the customise button
     */
    private void SetupCutomiseOnClick() {
        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "Customise button Clicked");
            }
        });
    }

    /**
     * On click listener for the view records button
     */
    private void SetupViewRecordOnClick() {
        btnViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "View Record button clicked");
                Bundle b = new Bundle();
                b.putString(FROM_KEY, "HomeActivity");
                b.putString(USER_NAME_KEY,currentUser);
                Intent i = new Intent(getBaseContext(),ViewRecordActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    /**
     * On click listener for the new record button
     */
    private void SetupNewRecordOnClick() {
        btnNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "New record button clicked");
                Intent i = new Intent(getBaseContext(), NewRecordActivity.class);
                Bundle data = new Bundle();
                data.putString(FROM_KEY,"HomeActivity");
                data.putString(USER_NAME_KEY,currentUser);
                i.putExtras(data);
                startActivity(i);
            }
        });
    }

    /**
     * This function sets up the layout for the admin control button
     */
    private void SetupAdminControlLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnAdminControl.setGravity(Gravity.CENTER);
        btnAdminControl.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the customise layout button
     */
    private void SetupCustomizeLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.45);
        btnCustomize.setGravity(Gravity.CENTER);
        btnCustomize.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the new record button
     */
    private void SetupNewRecordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.15);
        btnNewRecord.setGravity(Gravity.CENTER);
        btnNewRecord.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the view record button
     */
    private void SetupViewRecordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        btnViewRecords.setGravity(Gravity.CENTER);
        btnViewRecords.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the title
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
