package com.needham.thomas.medicare.root.Activities;

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
import android.widget.Toast;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FontSizeActivity extends FragmentActivity implements IAppConstants {

    int width;
    int height;
    TextView title;
    Button btnSmall;
    Button btnMedium;
    Button btnLarge;
    DisplayMetrics dm;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        UnpackBundle();
        SetupViews();
    }

    private void UnpackBundle() {
        Bundle b = getIntent().getExtras();
        if(b == null)
            throw new Error("null bundle found");
        if(b.getString(FROM_KEY,"").equals("CustomisationActivity")){
            currentUser = b.getString(USER_NAME_KEY,"");
            if(currentUser.equals(""))
                throw new Error("Invalid user logged in");
        }
        else
            throw new Error("Invalid bundle Found");
    }
    private void SetupViews() {
        title = (TextView) findViewById(R.id.fontSize_Title);
        btnSmall = (Button) findViewById(R.id.btnSmallFont);
        btnMedium = (Button) findViewById(R.id.btnMediumFont);
        btnLarge = (Button) findViewById(R.id.btnLargeFont);
        SetupTitleLayout();
        SetupSmallFontLayout();
        SetupMediumFontLayout();
        SetupLargeFontLayout();
        SetupSmallFontOnClick();
        SetupMediumFontOnClick();
        SetupLargeFontOnClick();
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

    private void SetupLargeFontOnClick() {
        btnLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Large Font button Clicked");
                UserDetails users = ReadUsers();
                SetFontSize(btnLarge.getTextSize(), users);
            }
        });
    }

    private void SetFontSize(float textSize, UserDetails users) {
        for(User u : users.getUserinfo()){
            if(u.getUserName().equals(currentUser)){
                u.setFontSize(textSize);
                Toast.makeText(getBaseContext(),"Settings saved restart the app to view changes", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        throw new Error("User " + currentUser + " not found");
    }

    private void SetupMediumFontOnClick() {
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Medium Font button Clicked");
            }
        });
    }

    private void SetupSmallFontOnClick() {
        btnSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customisation", "Small Font button Clicked");
            }
        });
    }

    private void SetupLargeFontLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnLarge.setGravity(Gravity.CENTER);
        btnLarge.setLayoutParams(params);
    }

    private void SetupMediumFontLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        btnMedium.setGravity(Gravity.CENTER);
        btnMedium.setLayoutParams(params);
    }

    private void SetupSmallFontLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.20);
        btnSmall.setGravity(Gravity.CENTER);
        btnSmall.setLayoutParams(params);
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
