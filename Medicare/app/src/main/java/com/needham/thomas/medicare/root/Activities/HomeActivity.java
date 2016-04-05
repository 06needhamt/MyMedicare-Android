package com.needham.thomas.medicare.root.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.Contact;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Permission;
import java.util.ArrayList;

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
    Button btnSendText;
    ArrayList<Contact> contacts;
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
        btnSendText = (Button) findViewById(R.id.btnSendText);
        SetupTitleLayout();
        SetupNewRecordLayout();
        SetupViewRecordLayout();
        SetupCustomizeLayout();
        SetupAdminControlLayout();
        SetupLogoutLayout();
        SetupSendTextLayout();
        SetupNewRecordOnClick();
        SetupViewRecordOnClick();
        SetupCutomiseOnClick();
        SetupAdminControlOnClick();
        SetupLogoutOnClick();
        SetupSendTextOnClick();
        ApplyUserSettings();
    }

    private ArrayList<Contact> GetContactsList() {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        assert cursor != null;
        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contact contact = new Contact(name,phoneNumber);
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    private void SetupSendTextOnClick() {
        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("text", "Send Text button Clicked");
                contacts = GetContactsList();
                Bundle b = new Bundle();
                b.putString(FROM_KEY, "HomeActivity");
                b.putString(USER_NAME_KEY, currentUser);
                b.putParcelableArrayList(CONTACTS_LIST_KEY, contacts);
                Intent i = new Intent(getBaseContext(),SendTextActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    //This is required for Android Marshmallow API 23+ to request permissions.
    // I could not get it working so I downgraded the app to Lollipop API 22 for now

//    private void AskForPermission() {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                builder.setTitle("Contacts access needed");
//                builder.setPositiveButton(android.R.string.ok, null);
//                builder.setMessage("please confirm Contacts access");
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        requestPermissions(
//                                new String[]
//                                        {Manifest.permission.READ_CONTACTS}
//                                , MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//                    }
//                });
//            }
//            else {
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//            }
//        }
//        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == (int) PackageManager.PERMISSION_GRANTED)
//        contacts = GetContactsList();
//    }

    /**
     * On Click listener for the logout button
     */
    private synchronized void SetupLogoutOnClick() {
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
                Bundle b = new Bundle();
                b.putString(FROM_KEY, "HomeActivity");
                b.putString(USER_NAME_KEY, currentUser);
                Intent i = new Intent(getBaseContext(), CustomisationActivity.class);
                i.putExtras(b);
                startActivity(i);
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
                data.putString(FROM_KEY, "HomeActivity");
                data.putString(USER_NAME_KEY, currentUser);
                i.putExtras(data);
                startActivity(i);
            }
        });
    }


    /**
     * This function applies the users chosen customised settings
     */
    private void ApplyUserSettings() {
        UserDetails users = ReadUsers();
        assert users != null;
        assert users.getUserinfo() != null;
        for(User u : users.getUserinfo()){
            if(u.getUserName().equals(currentUser)){
                if(u.getFontSize() != 0.0f) {
                    btnNewRecord.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnAdminControl.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                    btnViewRecords.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                    btnCustomize.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                    btnLogout.setTextSize(TypedValue.COMPLEX_UNIT_PX,u.getFontSize());
                }
                if(u.getBackgroundColour() != 0){
                    FrameLayout layout = (FrameLayout) findViewById(R.id.homeRoot);
                    layout.setBackgroundColor(getResources().getColor(u.getBackgroundColour()));
                }
                if(u.getFontColour() != 0){
                    title.setTextColor(getResources().getColor(u.getFontColour()));
                    btnNewRecord.setTextColor(getResources().getColor(u.getFontColour()));
                    btnAdminControl.setTextColor(getResources().getColor(u.getFontColour()));
                    btnViewRecords.setTextColor(getResources().getColor(u.getFontColour()));
                    btnCustomize.setTextColor(getResources().getColor(u.getFontColour()));
                    btnLogout.setTextColor(getResources().getColor(u.getFontColour()));
                }
                break;
            }
        }
    }

    private void SetupSendTextLayout(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnSendText.setGravity(Gravity.CENTER);
        btnSendText.setLayoutParams(params);
    }

    /**
     * This function sets up the layout for the logout button
     */
    private void SetupLogoutLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.50);
        btnLogout.setGravity(Gravity.CENTER);
        btnLogout.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the admin control button
     */
    private void SetupAdminControlLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
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
        params.topMargin = (int) (height * 0.30);
        btnCustomize.setGravity(Gravity.CENTER);
        btnCustomize.setLayoutParams(params);
    }



    /**
     * This function sets up the layout for the view record button
     */
    private void SetupViewRecordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.20);
        btnViewRecords.setGravity(Gravity.CENTER);
        btnViewRecords.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the new record button
     */
    private void SetupNewRecordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.10);
        btnNewRecord.setGravity(Gravity.CENTER);
        btnNewRecord.setLayoutParams(params);
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
