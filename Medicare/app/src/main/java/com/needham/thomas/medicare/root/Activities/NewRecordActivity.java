package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.EnumRiskLevel;
import com.needham.thomas.medicare.root.Classes.EnumTemperatureUnit;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.Record;
import com.needham.thomas.medicare.root.Classes.RecordList;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;
import com.needham.thomas.medicare.root.dialogs.IConfirmInputDialogCompliant;
import com.needham.thomas.medicare.root.dialogs.InvalidInputDialogFragment;
import com.needham.thomas.medicare.root.dialogs.RiskLevelDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class NewRecordActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant {

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
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        dm = new DisplayMetrics();
        UnpackBundle();
        GetDisplayMetrics(dm);
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
     * This function sets up the new record user interface
     */
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
        SetupRadioButtonLayout();
        SetupTemperatureLayout();
        SetupBloodPressureLowLayout();
        SetupBloodPressureHighLayout();
        SetupHeartRateLayout();
        SetupSubmitLayout();
        SetupSubmitOnClick();
        ApplyUserSettings();
    }

    private void ApplyUserSettings() {
        UserDetails users = ReadUsers();
        assert users != null;
        assert users.getUserinfo() != null;
        for (User u : users.getUserinfo()) {
            if (u.getUserName().equals(currentUser)) {
                if (u.getFontSize() != 0.0f) {
                    txtTemperature.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    txtHeartRate.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    txtBloodPressureLow.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    txtBloodPressureHigh.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnNewRecordSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());

                }
                if (u.getBackgroundColour() != 0) {
                    FrameLayout layout = (FrameLayout) findViewById(R.id.newRecordRoot);
                    layout.setBackgroundColor(getResources().getColor(u.getBackgroundColour()));
                }
                if (u.getFontColour() != 0) {
                    title.setTextColor(getResources().getColor(u.getFontColour()));
                    txtTemperature.setTextColor(getResources().getColor(u.getFontColour()));
                    txtHeartRate.setTextColor(getResources().getColor(u.getFontColour()));
                    txtBloodPressureLow.setTextColor(getResources().getColor(u.getFontColour()));
                    txtBloodPressureHigh.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());
                    btnNewRecordSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, u.getFontSize());

                }
                break;
            }
        }
    }
    /**
     * On click listener for the submit button
     */
    private void SetupSubmitOnClick() {
        btnNewRecordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New Record", "Submit button clicked");
                Record r = CreateRecord();
                if(!WriteRecord(r)){
                    throw new Error("An Error occurred while writing the record to a file");
                }
                SpawnRiskLevelDialog(r);
            }
        });
    }

    private void SpawnRiskLevelDialog(Record r) {
        RiskLevelDialogFragment fragment = new RiskLevelDialogFragment("Your risk level is: " + r.getRiskLevel().name(),this);
        fragment.show(getFragmentManager(), "dia");
    }

    /**
     * This function writes a record to the records file
     * @param r the record to write
     * @return whether the operation succeeded
     */
    private boolean WriteRecord(Record r) {

        try {
            File file = new File(getFilesDir().getAbsolutePath(), RECORD_FILE_NAME);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            Log.e("File Path", file.getAbsolutePath());
            RecordList recordList;
            if(file.length() != 0){ // if there are previously saved users
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                recordList = (RecordList) objectInputStream.readObject();
            }
            else{
                recordList = RecordList.GetInstance();
            }

            Log.e("List Size",String.valueOf(recordList.getRecords().size()));
            recordList.addRecord(r);
            Log.e("File Size",String.valueOf(file.length()));
            Log.e("List Size ", String.valueOf(recordList.getRecords().size()));
            FileOutputStream fileOutputStream = new FileOutputStream(file,false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(recordList);
            Log.e("File Size", String.valueOf(file.length()));

        }
        catch (IOException ex){
            ex.printStackTrace();
            Log.e("WriteRecord","An error occurred while writing the Record to a file");
            throw new Error("\"An error occurred while writing the Record to a file\"");
        }
        catch (ClassNotFoundException cls) {
            cls.printStackTrace();
            Log.e("ReadAppointment", "Class Not Found");
            throw new Error("Class Not Found");
        }
        return true;
    }

    /**
     * This function creates a record object from the inputted values
     * @return the created object
     */
    private Record CreateRecord() {
        Record r;
        EnumTemperatureUnit unit;
        if(rdbCelcius.isChecked())
            unit = EnumTemperatureUnit.CELCIUS;
        else if(rdbFahrenheight.isChecked())
            unit = EnumTemperatureUnit.FAHRENHEIT;
        else {
            InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("Please select a unit", this);
            fragment.show(getFragmentManager(),"dia");
            return null;
        }
        double temperature = Double.parseDouble(txtTemperature.getText().toString());
        int bloodlow = Integer.parseInt(txtBloodPressureLow.getText().toString());
        int bloodhigh = Integer.parseInt(txtBloodPressureHigh.getText().toString());
        int heartRate = Integer.parseInt(txtHeartRate.getText().toString());
        EnumRiskLevel riskLevel = CalculateRiskLevel(unit, temperature, bloodlow, bloodhigh, heartRate);
        User loggedInUser = GetLoggedInUser();
        Date timeTaken = new Date();
        r = new Record(loggedInUser,riskLevel,unit,temperature,bloodlow,bloodhigh,heartRate, timeTaken);
        return r;
    }

    /**
     * This function gets the currently logged in user
     * @return the user object for the currently logged user
     */
    private User GetLoggedInUser() {
        UserDetails details = ReadUsers();
        if(details == null)
            throw new Error("No users found");
        for(User u : details.getUserinfo()){
            if(u.getUserName().equals(currentUser))
                return u;
        }
        throw new Error("User: " + currentUser + " Not found");
    }

    /**
     * This function calculates the risk level for the current record
     * @param unit the unit of temperature  i.e. Celcius or Farenheit
     * @param temperature the temperature value
     * @param bloodlow the low blood pressure reading
     * @param bloodhigh the high blood pressure reading
     * @param heartRate the heart rate reading
     * @return the calculated risk level i.e. low medium or high
     */
    private EnumRiskLevel CalculateRiskLevel(EnumTemperatureUnit unit, double temperature, int bloodlow, int bloodhigh, int heartRate) {
        if(unit == EnumTemperatureUnit.FAHRENHEIT){
            if(temperature <= 98.6 && bloodlow < 80 && bloodhigh < 120 && heartRate <= 72)
                return EnumRiskLevel.LOW;
            else if((temperature >= 98.6 && temperature <= 100.4) &&
                    (bloodlow >= 0 && bloodlow < 110) &&
                    (bloodhigh >= 0 && bloodhigh < 180)
                    && heartRate < 160)
                return EnumRiskLevel.MEDIUM;
            else
                return EnumRiskLevel.HIGH;
        }
        else{
            if(temperature <= 37 && bloodlow < 80 && bloodhigh < 120 && heartRate <= 72)
                return EnumRiskLevel.LOW;
            else if((temperature >= 37 && temperature <= 38) &&
                    (bloodlow >= 0 && bloodlow < 110) &&
                    (bloodhigh >= 0 && bloodhigh < 180)
                    && heartRate < 160)
                return EnumRiskLevel.MEDIUM;
            else
                return EnumRiskLevel.HIGH;
        }
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
     * this function sets up the user interface for the submit button
     */
    private void SetupSubmitLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.60);
        btnNewRecordSubmit.setGravity(Gravity.CENTER);
        btnNewRecordSubmit.setLayoutParams(params);
    }

    /**
     * This function sets up the user interface for the heart rate edit text
     */
    private void SetupHeartRateLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.50);
        txtHeartRate.setGravity(Gravity.CENTER);
        txtHeartRate.setLayoutParams(params);
    }
    /**
     * This function sets up the user interface for the blood pressure high edit text
     */
    private void SetupBloodPressureHighLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.40);
        txtBloodPressureHigh.setGravity(Gravity.CENTER);
        txtBloodPressureHigh.setLayoutParams(params);
    }
    /**
     * This function sets up the user interface for the blood pressure low edit text
     */
    private void SetupBloodPressureLowLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.30);
        txtBloodPressureLow.setGravity(Gravity.CENTER);
        txtBloodPressureLow.setLayoutParams(params);
    }
    /**
     * This function sets up the user interface for the temperature edit text
     */
    private void SetupTemperatureLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.20);
        txtTemperature.setGravity(Gravity.CENTER);
        txtTemperature.setLayoutParams(params);
    }
    /**
     * This function sets up the user interface for the temperature unit radio buttons
     */
    private void SetupRadioButtonLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.10);
        rdgTemperatureUnit.setGravity(Gravity.CENTER);
        rdgTemperatureUnit.setLayoutParams(params);
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

    /**
     * This method is called when the positive button is pressed on the dialog Fragment
     *
     * @param input the input entered into the dialog fragment by the user
     * @see AlertDialog.Builder#setPositiveButton(CharSequence, DialogInterface.OnClickListener)
     */
    @Override
    public void doYesConfirmClick(String input) {
        Toast.makeText(getBaseContext(),"Record successfully saved", Toast.LENGTH_LONG).show();
        finish();
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
