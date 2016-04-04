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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;

public class CustomisationActivity extends FragmentActivity implements IAppConstants {

    int width;
    int height;
    TextView title;
    Button btnFontSize;
    Button btnFontColour;
    Button btnBackgroundColour;
    DisplayMetrics dm;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customisation);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        UnpackBundle();
        SetupViews();
    }

    private void UnpackBundle() {
        Bundle b = getIntent().getExtras();
        if(b == null)
            throw new Error("null bundle found");
        if(b.getString(FROM_KEY,"").equals("HomeActivity")){
            currentUser = b.getString(USER_NAME_KEY,"");
            if(currentUser.equals(""))
                throw new Error("Invalid user logged in");
        }
        else
            throw new Error("Invalid bundle Found");
    }

    private void SetupViews() {
        title = (TextView) findViewById(R.id.customisation_Title);
        btnFontSize = (Button) findViewById(R.id.btnFontSize);
        btnFontColour = (Button) findViewById(R.id.btnFontColour);
        btnBackgroundColour = (Button) findViewById(R.id.btnBackgroundColour);
        SetupTitleLayout();
        SetupFontSizeLayout();
        SetupFontColourLayout();
        SetupBackgroundColourLayout();
        SetupFontSizeOnClick();
        SetupFontColourOnClick();
        SetupBackgroundColourOnClick();
    }

    private void SetupBackgroundColourOnClick() {
        btnBackgroundColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Background Colour button Clicked");
            }
        });
    }

    private void SetupFontColourOnClick() {
        btnFontColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Font Colour button Clicked");
            }
        });
    }

    private void SetupFontSizeOnClick() {
        btnFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Font Size button Clicked");
                Bundle b = new Bundle();
                b.putString(FROM_KEY,"CustomisationActivity");
                b.putString(USER_NAME_KEY,currentUser);
                Intent i = new Intent(getBaseContext(), FontSizeActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void SetupBackgroundColourLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnBackgroundColour.setGravity(Gravity.CENTER);
        btnBackgroundColour.setLayoutParams(params);
    }

    private void SetupFontColourLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        btnFontColour.setGravity(Gravity.CENTER);
        btnFontColour.setLayoutParams(params);
    }

    private void SetupFontSizeLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.20);
        btnFontSize.setGravity(Gravity.CENTER);
        btnFontSize.setLayoutParams(params);
    }

    /**
     * This function sets up the user interface for the title
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
