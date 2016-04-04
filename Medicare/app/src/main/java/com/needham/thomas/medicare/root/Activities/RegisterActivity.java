package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
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

/**
 * Created by Thomas Needham on 01/04/2016.
 */
public class RegisterActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
        if(!CreateAdminUser()){
            Log.e("Error", "Error creating admin user");
            throw new Error("Error creating admin user");
        }
    }

    /**
     * This function creates the admin user
     * @return whether the operation completed successfully
     */
    private boolean CreateAdminUser() {
        User admin = new User("admin","root","admin","admin",9001,"admin","admin","admin","admin");
        return WriteFile(admin);
    }

    /**
     * This function sets up the register activity user interface
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.register_title);
        txtuserName = (EditText) findViewById(R.id.txtRegisterUsername);
        txtfirstName = (EditText) findViewById(R.id.txtRegisterFirstName);
        txtLastName = (EditText) findViewById(R.id.txtRegisterLastName);
        txtage = (EditText) findViewById(R.id.txtRegisterAge);
        txtaddress1 = (EditText) findViewById(R.id.txtRegisterAddress1);
        txtaddress2 = (EditText) findViewById(R.id.txtRegisterAddress2);
        txtphoneNumber = (EditText) findViewById(R.id.txtRegisterPhoneNumber);
        txtnurseName = (EditText) findViewById(R.id.txtRegisterNurseName);
        btnSubmit = (Button) findViewById(R.id.btnRegisterSubmit);
        txtPassword = (EditText) findViewById(R.id.txtRegisterPassword);
        txtReenterPassword = (EditText) findViewById(R.id.txtRegisterReenterPassword);
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
    }


    /**
     * On click listener for the submit button
     */
    private void SetupSubmitOnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Register Submit button clicked");
                if (!CreateUser()) {
                    Log.e("ERROR:", "An error occurred while writing user details");
                    return;
                }
                Log.e("User", "User details successfully written to file");
            }
        });
    }

    /**
     * This function creates a user object from the inputted values
     * @return whether the operation succeeded
     */
    private boolean CreateUser() {
        String userName = txtuserName.getText().toString();
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
        User user = new User(userName, password, firstName, lastName, age, address1, address2, phoneNumber, nurseName);
//        details.addUser(user);
        if(!WriteFile(user)){
            Log.e("n", "writing");
            return false;
        }
        return true;
    }

    /**
     * This function writes a user object to the user file.
     * @param user the user object to write
     * @return weather the operation succeeded
     */
    private boolean WriteFile(User user) {
        try {
            File file = new File(getFilesDir().getAbsolutePath(), USER_FILE_NAME);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            Log.e("File Path", file.getAbsolutePath());
            UserDetails details;
            if(file.length() != 0){ // if there are previously saved users
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                details = (UserDetails) objectInputStream.readObject();
                for(User u : details.getUserinfo()){
                    if(user.getUserName().equals("admin") && u.getUserName().equals("admin")){ // don't recreate the admin user
                        Log.e("Admin", "Admin already exists");
                        return true;
                    }
                    if(u.getUserName().equals(user.getUserName())){
                        Log.e("n", "duplicate");
                        InvalidInputDialogFragment frag = new InvalidInputDialogFragment("Username is already taken",this);
                        frag.show(getFragmentManager(), "dia");
                        return false;
                    }
                }
            }
            else{
                details = UserDetails.GetInstance();
            }

            Log.e("List Size",String.valueOf(details.getUserinfo().size()));
            details.addUser(user);
            Log.e("File Size",String.valueOf(file.length()));
            Log.e("List Size ", String.valueOf(details.getUserinfo().size()));
            FileOutputStream fileOutputStream = new FileOutputStream(file,false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(details);
            Log.e("File Size", String.valueOf(file.length()));
            if(!user.getUserName().equals("admin")) { // Don't show registration message for admin
                Toast.makeText(getBaseContext(), "User successfully registered", Toast.LENGTH_LONG).show();
                finish();
            }

        }
        catch (IOException ex){
            ex.printStackTrace();
            Log.e("WriteUser","An error occurred while writing the user to a file");
            throw new Error("\"An error occurred while writing the user to a file\"");
        }
        catch (ClassNotFoundException cls) {
            cls.printStackTrace();
            Log.e("ReadAppointment", "Class Not Found");
            throw new Error("Class Not Found");
        }
        return true;
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
