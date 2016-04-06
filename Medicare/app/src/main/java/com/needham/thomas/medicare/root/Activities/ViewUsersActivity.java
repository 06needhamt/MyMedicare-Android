package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.needham.thomas.medicare.R;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.ListViewBorder;
import com.needham.thomas.medicare.root.Classes.Record;
import com.needham.thomas.medicare.root.Classes.RecordList;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;
import com.needham.thomas.medicare.root.dialogs.DeleteRecordConfirmDialogFragment;
import com.needham.thomas.medicare.root.dialogs.IConfirmInputDialogCompliant;
import com.needham.thomas.medicare.root.dialogs.InvalidInputDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ViewUsersActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant {

    int width;
    int height;
    int pos;
    DisplayMetrics dm;
    TextView title;
    ListView lstUsers;
    UserDetails users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        SetupViews();
    }

    private void SetupViews() {
        title = (TextView) findViewById(R.id.viewUsersTitle);
        lstUsers = (ListView) findViewById(R.id.lstUsersList);
        SetupTitleLayout();
        SetupUsersListLayout();
        SetupListItemClickListener();
        PopulateList();
    }

    private void PopulateList() {
        UserDetails userDetails = ReadUsers();
        assert userDetails != null;
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getBaseContext(),R.layout.list_view_text,userDetails.getUserinfo());
        lstUsers.setAdapter(adapter);
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
     * On long click listener for records held in the record list view
     */
    private void SetupListItemClickListener() {
        lstUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                "Are You Sure You Want To Delete This User?", this);
        fragment.show(getFragmentManager(), "dialog");

    }

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
            Log.e("WriteUser", "An error occurred while writing the user to a file");
            throw new Error("\"An error occurred while writing the user to a file\"");
        }
        return true;
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
    private void SetupUsersListLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = (int) (height * 0.70);
        params.topMargin = (int) (height * 0.10);
        lstUsers.setLayoutParams(params);
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
        users = ReadUsers();
        RecordList records = GetRecords();
        ListAdapter adapter = lstUsers.getAdapter();
        assert users != null;
        ArrayList<User> temp = users.getUserinfo();
        if(temp.get(pos).getUserName().equals("admin")){
            InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("admin can not be deleted");
            fragment.show(getFragmentManager(),"dia");
            return;
        }
        for(Record rec : records.getRecords()){
            if(rec.getOwner().getUserName().equals(temp.get(pos).getUserName())){ // delete records belonging to the user that is getting deleted
                records.getRecords().remove(rec);
            }
        }
        temp.remove(pos);
        users.setUserinfo(temp);
        WriteRecords(records);
        WriteUserDetails(users);
        PopulateList();
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
