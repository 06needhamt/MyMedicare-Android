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

    private void UnpackBundle() {
        Bundle b = getIntent().getExtras();
        currentUser = b.getString(USER_NAME_KEY,"");
        // password and other details will be needed later
    }

    private void SetupViews() {
        title = (TextView) findViewById(R.id.menu_title);
        btnNewRecord = (Button) findViewById(R.id.btnNewReading);
        btnViewRecords = (Button) findViewById(R.id.btn_ViewReadings);
        btnCustomize = (Button) findViewById(R.id.btnCustomise);
        btnAdminControl = (Button) findViewById(R.id.btnAdminControl);
        SetupTitleLayout();
        SetupNewRecordLayout();
        SetupViewRecordLayout();
        SetupCustomizeLayout();
        SetupAdminControlLayout();
        SetupNewRecordOnClick();
        SetupViewRecordOnClick();
        SetupCutomiseOnClick();
        SetupAdminControlOnClick();
    }

    private void SetupAdminControlOnClick() {
        btnAdminControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "Admin Control Button Clicked");
            }
        });
    }

    private void SetupCutomiseOnClick() {
        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "Customise button Clicked");
            }
        });
    }

    private void SetupViewRecordOnClick() {
        btnViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home", "View Record button clicked");
            }
        });
    }

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

    private void SetupAdminControlLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnAdminControl.setGravity(Gravity.CENTER);
        btnAdminControl.setLayoutParams(params);
    }

    private void SetupCustomizeLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.45);
        btnCustomize.setGravity(Gravity.CENTER);
        btnCustomize.setLayoutParams(params);
    }

    private void SetupNewRecordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.15);
        btnNewRecord.setGravity(Gravity.CENTER);
        btnNewRecord.setLayoutParams(params);
    }

    private void SetupViewRecordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        btnViewRecords.setGravity(Gravity.CENTER);
        btnViewRecords.setLayoutParams(params);
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
