package com.needham.thomas.medicare.root.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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

    /**
     * This function unpacks the bundle containing the username of the currently logged in user
     */
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

    /**
     * This function sets up the user interface for the customisation activity
     */
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
        ApplyUserSettings();
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
    private void ApplyUserSettings() {
        UserDetails users = ReadUsers();
        assert users != null;
        assert users.getUserinfo() != null;
        for (User u : users.getUserinfo()) {
            if (u.getUserName().equals(currentUser)) {
                if (u.getFontSize() != 0.0f) {
                    btnBackgroundColour.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnFontSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnFontColour.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                }
                if (u.getBackgroundColour() != 0) {
                    FrameLayout layout = (FrameLayout) findViewById(R.id.customisationRoot);
                    layout.setBackgroundColor(getResources().getColor(u.getBackgroundColour()));
                }
                if (u.getFontColour() != 0) {
                    title.setTextColor(getResources().getColor(u.getFontColour()));
                    btnBackgroundColour.setTextColor(getResources().getColor(u.getFontColour()));
                    btnFontSize.setTextColor(getResources().getColor(u.getFontColour()));
                    btnFontColour.setTextColor(getResources().getColor(u.getFontColour()));
                }
                break;
            }
        }
    }
        /**
     * On Click listener for the set background colour button
     */
    private void SetupBackgroundColourOnClick() {
        btnBackgroundColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Background Colour button Clicked");
                Bundle b = new Bundle();
                b.putString(FROM_KEY,"CustomisationActivity");
                b.putString(USER_NAME_KEY,currentUser);
                b.putString(COLOUR_TYPE_KEY, BACKGROUND_COLOUR_TYPE);
                Intent i = new Intent(getBaseContext(),ColourActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
    /**
     * On Click listener for the set font colour button
     */
    private void SetupFontColourOnClick() {
        btnFontColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Font Colour button Clicked");
                Bundle b = new Bundle();
                b.putString(FROM_KEY,"CustomisationActivity");
                b.putString(USER_NAME_KEY,currentUser);
                b.putString(COLOUR_TYPE_KEY, FONT_COLOUR_TYPE);
                Intent i = new Intent(getBaseContext(),ColourActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
    /**
     * On Click listener for the set font size button
     */
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
    /**
     * This function sets up the layout for the set background colour button
     */
    private void SetupBackgroundColourLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnBackgroundColour.setGravity(Gravity.CENTER);
        btnBackgroundColour.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the set font colour button
     */
    private void SetupFontColourLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        btnFontColour.setGravity(Gravity.CENTER);
        btnFontColour.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the set font size button
     */
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
