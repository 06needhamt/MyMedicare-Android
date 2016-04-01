package com.needham.thomas.medicare.root.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.needham.thomas.medicare.R;

public class NewRecordActivity extends AppCompatActivity {

    int width;
    int height;
    TextView title;
    RadioGroup rdgTemperatureUnit;
    RadioButton rdbCelcius;
    RadioButton rdbFahrenheight;
    EditText txtTemperature;
    EditText txtBloodPressureLow;
    EditText txtBloodPressureHigh;
    EditText txtHeartRate;
    Button btnNewRecordSubmit;
    DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    private void SetupViews() {
        title = (TextView) findViewById(R.id.newRecordTitle);
        rdgTemperatureUnit = (RadioGroup) findViewById(R.id.rgdTemperatureUnit);
        rdbCelcius = (RadioButton) findViewById(R.id.rdbCelcius);
        rdbFahrenheight = (RadioButton) findViewById(R.id.rdbFarenheight);
        txtTemperature = (EditText) findViewById(R.id.txtTemperature);
        txtBloodPressureLow = (EditText) findViewById(R.id.txtBloodPressureLow);
        txtBloodPressureHigh = (EditText) findViewById(R.id.txtBloodPressureHigh);
        txtHeartRate = (EditText) findViewById(R.id.txtHeartRate);
        btnNewRecordSubmit = (Button) findViewById(R.id.btnNewRecordSubmit);
        SetupTitleLayout();

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
