package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;
import com.needham.thomas.medicare.root.dialogs.IConfirmInputDialogCompliant;
import com.needham.thomas.medicare.root.dialogs.InvalidInputDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ModifyInfoActivity extends FragmentActivity implements IAppConstants,IConfirmInputDialogCompliant {

    private int width;
    private int height;
    private TextView title;
    private EditText txtuserName;
    private EditText txtfirstName;
    private EditText txtLastName;
    private EditText txtage;
    private EditText txtaddress1;
    private EditText txtaddress2;
    private EditText txtphoneNumber;
    private EditText txtnurseName;
    private EditText txtPassword;
    private EditText txtReenterPassword;
    private Button btnSubmit;
    private DisplayMetrics dm;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        dm = new DisplayMetrics();
        UnpackBundle();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    /**
     * This function unpacks a bundle containing the currently logged in user's username
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
     * This function sets up the Modify activity user interface
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.modify_title);
        txtuserName = (EditText) findViewById(R.id.txtmodifyUsername);
        txtfirstName = (EditText) findViewById(R.id.txtmodifyFirstName);
        txtLastName = (EditText) findViewById(R.id.txtmodifyLastName);
        txtage = (EditText) findViewById(R.id.txtmodifyAge);
        txtaddress1 = (EditText) findViewById(R.id.txtmodifyAddress1);
        txtaddress2 = (EditText) findViewById(R.id.txtmodifyAddress2);
        txtphoneNumber = (EditText) findViewById(R.id.txtmodifyPhoneNumber);
        txtnurseName = (EditText) findViewById(R.id.txtmodifyNurseName);
        btnSubmit = (Button) findViewById(R.id.btnmodifySubmit);
        txtPassword = (EditText) findViewById(R.id.txtmodifyPassword);
        txtReenterPassword = (EditText) findViewById(R.id.txtmodifyReenterPassword);
        SetupTitleLayout();
        SetupUserNameLayout();
        SetupFirstNameLayout();
        SetupLastNameLayout();
        SetupAgeLayout();
        SetupAddress1Layout();
        SetupAddress2Layout();
        SetupPhoneNumberLayout();
        SetupNurseNameLayout();
        SetupPasswordLayout();
        SetupReEnterPasswordLayout();
        SetupSubmitLayout();
        SetupSubmitOnClick();
        PopulateFields();
        ApplyUserSettings();
    }

    /**
     * This function applies the current user's chosen customisation settings
     */
    private void ApplyUserSettings() {
        UserDetails users = ReadUsers();
        assert users != null;
        assert users.getUserinfo() != null;
        for (User u : users.getUserinfo()) {
            if (u.getUserName().equals(currentUser)) {
                if (u.getFontSize() != 0.0f) {
                    // here in case elements are added
                }
                if (u.getBackgroundColour() != 0) {
                    FrameLayout layout = (FrameLayout) findViewById(R.id.modifyInfoRoot);
                    layout.setBackgroundColor(getResources().getColor(u.getBackgroundColour()));
                }
                if (u.getFontColour() != 0) {
                    title.setTextColor(getResources().getColor(u.getFontColour()));
                }
                break;
            }
        }
    }

    /**
     * This function populates the input fields with the current details
     */
    private void PopulateFields() {
        UserDetails details = ReadUsers();
        assert details != null;
        for(User u : details.getUserinfo()){
            if(u.getUserName().equals(currentUser)){
                txtuserName.setText(u.getUserName());
                txtfirstName.setText(u.getFirstName());
                txtLastName.setText(u.getLastName());
                txtage.setText(String.valueOf(u.getAge()));
                txtaddress1.setText(u.getAddress1());
                txtaddress2.setText(u.getAddress2());
                txtphoneNumber.setText(u.getPhoneNumber());
                txtnurseName.setText(u.getNurseName());
                txtPassword.setText(u.getPassword());
                return;
            }
        }
    }



    /**
     * On click listener for the submit button
     */
    private void SetupSubmitOnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Modify Submit button clicked");
                if (!ModifyUser()) {
                    Log.e("ERROR:", "An error occurred while writing user details");
                    return;
                }
                Log.e("User", "User details successfully written to file");
                Toast.makeText(getBaseContext(), "User details successfully written to file", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /**
     * This function writes the modified user details to the user file
     * @return whether the operation completed successfully
     */
    private boolean ModifyUser() {
        UserDetails details = ReadUsers();
        String userName = txtuserName.getText().toString();
        if(!userName.equals(currentUser)){
            InvalidInputDialogFragment frag = new InvalidInputDialogFragment("User name can not be modified",this);
            frag.show(getFragmentManager(),"dia");
            return false;
        }
        String firstName = txtfirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        if(txtage.getText().toString().length() == 0){
            InvalidInputDialogFragment frag = new InvalidInputDialogFragment("All fields must be filled",this);
            frag.show(getFragmentManager(),"dia");
            return false;
        }
        int age = Integer.parseInt(txtage.getText().toString());
        if(age < 0){
            Log.e("n", "age");
            InvalidInputDialogFragment frag = new InvalidInputDialogFragment("Age must Not be negative",this);
            frag.show(getFragmentManager(),"dia");
            return false;
        }
        String address1 = txtaddress1.getText().toString();
        String address2 = txtaddress2.getText().toString();
        String phoneNumber = txtphoneNumber.getText().toString();
        String nurseName = txtnurseName.getText().toString();
        String password = txtPassword.getText().toString();
        String reEnteredPassword = txtReenterPassword.getText().toString();
        if(phoneNumber.length() != 11 || !phoneNumber.startsWith("0")){
            Log.e("n", "Invalid Phone Number");
            InvalidInputDialogFragment frag = new InvalidInputDialogFragment("Invalid Phone Number",this);
            frag.show(getFragmentManager(), "dia");
            return false;
        }
        if(!password.equals(reEnteredPassword)){
            Log.e("n", "Mismatched password");
            InvalidInputDialogFragment frag = new InvalidInputDialogFragment("Passwords do not match",this);
            frag.show(getFragmentManager(),"dia");
            return false;
        }
        if(userName.equals("") || firstName.equals("") || lastName.equals("") ||
                address1.equals("") || phoneNumber.equals("") || nurseName.equals("") ||
                password.equals("") || reEnteredPassword.equals("") ){
            Log.e("n", "fields");
            InvalidInputDialogFragment frag = new InvalidInputDialogFragment("All fields must be filled",this);
            frag.show(getFragmentManager(),"dia");
            return false;
        }
        assert details != null;
        for(User u : details.getUserinfo()) {
            if(u.getUserName().equals(currentUser)) {
                u.setUserName(userName);
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setAge(age);
                u.setAddress1(address1);
                u.setAddress2(address2);
                u.setPhoneNumber(phoneNumber);
                u.setNurseName(nurseName);
                u.setPassword(password);

                if(!WriteUserDetails(details)){
                    Toast.makeText(getBaseContext(), "An error occurred while writing user info", Toast.LENGTH_LONG).show();
                    return false;
                }
                Log.e("user", u.getUserName() + "\n"
                        + u.getFirstName() + "\n" +
                        u.getLastName() + "\n"
                        + u.getAge() + "\n"
                        + u.getAddress1() + "\n"
                        + u.getAddress2() + "\n"
                        + u.getPhoneNumber() + "\n"
                        + u.getNurseName() + "\n"
                        + u.getPassword() + "\n");
                return true;
            }
        }
        InvalidInputDialogFragment frag = new InvalidInputDialogFragment("User name can not be modified",this);
        frag.show(getFragmentManager(),"dia");
        return false;
    }

    /**
     * This function writes user details to the user file
     * @param details the details to write
     * @return whether the operation completed successfully
     */
    private boolean WriteUserDetails(UserDetails details) {
        try {
            File file = new File(getFilesDir().getAbsolutePath(), USER_FILE_NAME);
            Log.e("File Size", String.valueOf(file.length()));
            Log.e("List Size ", String.valueOf(details.getUserinfo().size()));
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(details);
            Log.e("File Size", String.valueOf(file.length()));
        }
        catch (IOException ex){
            ex.printStackTrace();
            Log.e("WriteUser","An error occurred while writing the user to a file");
            throw new Error("\"An error occurred while writing the user to a file\"");
        }
        return true;
    }

    /**
     * This function reads all currently registered users from the user file
     * @return an object containing a list of all users
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
     * This function sets up the layout for the submit button
     */
    private void SetupSubmitLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.77);
        btnSubmit.setGravity(Gravity.CENTER);
        btnSubmit.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the re-enter password edit text
     */
    private void SetupReEnterPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.70);
        txtReenterPassword.setGravity(Gravity.CENTER);
        txtReenterPassword.setLayoutParams(params);

    }
    /**
     * This function sets up the layout for the password edit text
     */
    private void SetupPasswordLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.63);
        txtPassword.setGravity(Gravity.CENTER);
        txtPassword.setLayoutParams(params);

    }
    /**
     * This function sets up the layout for the nurse name edit text
     */
    private void SetupNurseNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.56);
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
        params.topMargin = (int) (height * 0.49);
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
        params.topMargin = (int) (height * 0.42);
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
        params.topMargin = (int) (height * 0.35);
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
        params.topMargin = (int) (height * 0.28);
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
        params.topMargin = (int) (height * 0.21);
        txtLastName.setGravity(Gravity.CENTER);
        txtLastName.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the first name edit text
     */
    private void SetupFirstNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.14);
        txtfirstName.setGravity(Gravity.CENTER);
        txtfirstName.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the user name edit text
     */
    private void SetupUserNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.07);
        txtuserName.setGravity(Gravity.CENTER);
        txtuserName.setLayoutParams(params);
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

    /**
     * This method is called when the positive button is pressed on the dialog Fragment
     *
     * @param input the input entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setPositiveButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doYesConfirmClick(String input) {

    }

    /**
     * This method is called when the negative button is pressed on the dialog Fragment
     *
     * @param input the input entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setNegativeButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doNoConfirmClick(String input) {

    }
}
