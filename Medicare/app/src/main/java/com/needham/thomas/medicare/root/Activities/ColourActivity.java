package com.needham.thomas.medicare.root.Activities;

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
import android.widget.Toast;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ColourActivity extends FragmentActivity implements IAppConstants {

    int width;
    int height;
    TextView title;
    Button btnRed;
    Button btnGreen;
    Button btnBlue;
    Button btnDefault;
    DisplayMetrics dm;
    String currentUser;
    String colourType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour);
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
            colourType = b.getString(COLOUR_TYPE_KEY,"");
            if(!(colourType.equals("Font") || colourType.equals("Background")))
                throw new Error("Invalid Colour type found");
        }
        else
            throw new Error("Invalid bundle Found");
    }

    private void SetupViews() {
        title = (TextView) findViewById(R.id.ColourTitle);
        btnRed = (Button) findViewById(R.id.btnRed);
        btnGreen = (Button) findViewById(R.id.btnGreen);
        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnDefault = (Button) findViewById(R.id.btnDefault);
        SetupTitleLayout();
        SetupRedLayout();
        SetupGreenLayout();
        SetupBlueLayout();
        SetupDefaultLayout();
        SetupRedOnClick();
        SetupGreenOnClick();
        SetuBlueOnClick();
        SetupDefaultOnClick();
    }

    /**
     * This function writes a list of user details to the user file
     * @param userDetails the records to write
     * @return whether the operation succeeded
     */
    private boolean WriteUsers(UserDetails userDetails) {

        try {
            File file = new File(getFilesDir().getAbsolutePath(), USER_FILE_NAME);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            Log.e("File Path", file.getAbsolutePath());

            Log.e("List Size", String.valueOf(userDetails.getUserinfo().size()));
            Log.e("File Size", String.valueOf(file.length()));
            Log.e("List Size ", String.valueOf(userDetails.getUserinfo().size()));
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(userDetails);
            Log.e("File Size", String.valueOf(file.length()));

        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("WriteRecord", "An error occurred while writing the Record to a file");
            throw new Error("\"An error occurred while writing the Record to a file\"");
        }
        return true;
    }
    private void SetupDefaultOnClick() {
        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Colours", "Red Button Clicked");
                UserDetails details = ReadUsers();
                assert details != null;
                for(User u : details.getUserinfo()) {
                    if(u.getUserName().equals(currentUser)) {
                        if (colourType.equals(FONT_COLOUR_TYPE))
                            u.setFontColour(0);
                        else if (colourType.equals(BACKGROUND_COLOUR_TYPE))
                            u.setBackgroundColour(0);
                        else
                            throw new Error("Invalid Colour Type");
                        break;
                    }
                }
                WriteUsers(details);
                Toast.makeText(getBaseContext(),"Settings saved restart the app to view changes", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void SetuBlueOnClick() {
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Colours", "Red Button Clicked");
                UserDetails details = ReadUsers();
                assert details != null;
                for(User u : details.getUserinfo()) {
                    if(u.getUserName().equals(currentUser)) {
                        if (colourType.equals(FONT_COLOUR_TYPE))
                            u.setFontColour(R.color.bg_Blue);
                        else if (colourType.equals(BACKGROUND_COLOUR_TYPE))
                            u.setBackgroundColour(R.color.bg_Blue);
                        else
                            throw new Error("Invalid Colour Type");
                        break;
                    }
                }
                WriteUsers(details);
                Toast.makeText(getBaseContext(),"Settings saved restart the app to view changes", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void SetupGreenOnClick() {
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Colours", "Red Button Clicked");
                UserDetails details = ReadUsers();
                assert details != null;
                for(User u : details.getUserinfo()) {
                    if(u.getUserName().equals(currentUser)) {
                        if (colourType.equals(FONT_COLOUR_TYPE))
                            u.setFontColour(R.color.bg_Green);
                        else if (colourType.equals(BACKGROUND_COLOUR_TYPE))
                            u.setBackgroundColour(R.color.bg_Green);
                        else
                            throw new Error("Invalid Colour Type");
                        break;
                    }
                }
                WriteUsers(details);
                Toast.makeText(getBaseContext(),"Settings saved restart the app to view changes", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void SetupRedOnClick() {
        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Colours", "Red Button Clicked");
                UserDetails details = ReadUsers();
                assert details != null;
                for(User u : details.getUserinfo()) {
                    if(u.getUserName().equals(currentUser)) {
                        if (colourType.equals(FONT_COLOUR_TYPE))
                            u.setFontColour(R.color.bg_Pink);
                        else if (colourType.equals(BACKGROUND_COLOUR_TYPE))
                            u.setBackgroundColour(R.color.bg_Pink);
                        else
                            throw new Error("Invalid Colour Type");
                        break;
                    }
                }
                WriteUsers(details);
                Toast.makeText(getBaseContext(),"Settings saved restart the app to view changes", Toast.LENGTH_LONG).show();
                finish();
            }
        });
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


    private void SetupDefaultLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnDefault.setGravity(Gravity.CENTER);
        btnDefault.setLayoutParams(params);
    }

    private void SetupBlueLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.45);
        btnBlue.setGravity(Gravity.CENTER);
        btnBlue.setLayoutParams(params);
    }

    private void SetupGreenLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        btnGreen.setGravity(Gravity.CENTER);
        btnGreen.setLayoutParams(params);
    }

    private void SetupRedLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.15);
        btnRed.setGravity(Gravity.CENTER);
        btnRed.setLayoutParams(params);
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
