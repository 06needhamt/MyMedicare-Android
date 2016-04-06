package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.Record;
import com.needham.thomas.medicare.root.Classes.RecordList;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;
import com.needham.thomas.medicare.root.dialogs.DeleteRecordConfirmDialogFragment;
import com.needham.thomas.medicare.root.dialogs.IConfirmInputDialogCompliant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ViewRecordActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant {

    int width;
    int height;
    String currentUser;
    TextView title;
    ListView lstRecordList;
    DisplayMetrics dm;
    RecordList records;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);
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
     * This function sets up the user interface for ViewRecordActivity
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.viewRecordsTitle);
        lstRecordList = (ListView) findViewById(R.id.lstrecordsList);
        SetupTitleLayout();
        SetupRecordListLayout();
        PopulateListView();
        SetupListItemClickListener();
        ApplyUserSettings();
    }

    /**
     * On long click listener for records held in the record list view
     */
    private void SetupListItemClickListener() {
        lstRecordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("list", "long click received");
                pos = position;
                SpawnDialog();
                return true;
            }
        });
    }

    /**
     * This function spawns a dialog for the user to confirm that they want to delete the selected record
     */
    private void SpawnDialog() {
        DeleteRecordConfirmDialogFragment fragment = new DeleteRecordConfirmDialogFragment(
                "Are You Sure You Want To Delete This Record?", this);
        fragment.show(getFragmentManager(), "dialog");

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
                    // here in case elements are added
                }
                if (u.getBackgroundColour() != 0) {
                    FrameLayout layout = (FrameLayout) findViewById(R.id.viewRecordRoot);
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
     * this function populates the list view with all records belonging to the current user in the user file
     */
    private void PopulateListView() {
        records = GetRecords();
        ArrayList<Record> yourRecords = new ArrayList<>();
        if(currentUser.equals("admin")){ // show all records for admin user
            ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getBaseContext(),R.layout.list_view_text,records.getRecords());
            lstRecordList.setAdapter(adapter);
            return;
        }
        for(Record rec : records.getRecords()){ // only show records belonging to the currently logged in user if they are not admin
            if(rec.getOwner().getUserName().equals(currentUser))
                yourRecords.add(rec);
        }
        ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getBaseContext(),R.layout.list_view_text,yourRecords);
        lstRecordList.setAdapter(adapter);
        Log.e("list", "list successfully populated");
    }

    /**
     * This function gets all records from the user file
     * @return all of the records stored in the user file
     */
    private RecordList GetRecords() {
        try {
            File file = new File(getFilesDir().getAbsolutePath(), RECORD_FILE_NAME);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            Log.e("File Path", file.getAbsolutePath());
            RecordList recordList;
            if (file.length() != 0) { // if there are previously saved users
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                recordList = (RecordList) objectInputStream.readObject();
            } else {
                recordList = RecordList.GetInstance();
            }
            return recordList;
        }
        catch (IOException ex){
            ex.printStackTrace();
            Log.e("WriteRecord","An error occurred while reading the Record from a file");
            throw new Error("\"An error occurred while reading the Record from a file\"");
        }
        catch (ClassNotFoundException cls) {
            cls.printStackTrace();
            Log.e("ReadAppointment", "Class Not Found");
            throw new Error("Class Not Found");
        }

    }

    /**
     * This function writes a list of records to the user file
     * @param recordList the records to write
     * @return whether the operation succeeded
     */
    private boolean WriteRecords(RecordList recordList) {

        try {
            File file = new File(getFilesDir().getAbsolutePath(), RECORD_FILE_NAME);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            Log.e("File Path", file.getAbsolutePath());

            Log.e("List Size", String.valueOf(recordList.getRecords().size()));
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
        return true;
    }

    /**
     * This function sets up the layout for the record list view
     */
    private void SetupRecordListLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = (int) (height * 0.70);
        params.topMargin = (int) (height * 0.10);
        //lstRecordList.setGravity(Gravity.CENTER);
        lstRecordList.setLayoutParams(params);
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
        records = GetRecords();
        ListAdapter adapter = lstRecordList.getAdapter();
        ArrayList<Record> temp = records.getRecords();
        temp.remove(pos);
        records.setRecords(temp);
        WriteRecords(records);
        PopulateListView();
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
