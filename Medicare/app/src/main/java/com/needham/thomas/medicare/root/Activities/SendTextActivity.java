package com.needham.thomas.medicare.root.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import com.needham.thomas.medicare.root.Classes.Contact;
import com.needham.thomas.medicare.root.Classes.IAppConstants;
import com.needham.thomas.medicare.root.Classes.ListViewBorder;
import com.needham.thomas.medicare.root.Classes.User;
import com.needham.thomas.medicare.root.Classes.UserDetails;
import com.needham.thomas.medicare.root.dialogs.IConfirmInputDialogCompliant;
import com.needham.thomas.medicare.root.dialogs.InvalidInputDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SendTextActivity extends FragmentActivity implements IAppConstants, IConfirmInputDialogCompliant {

    int width;
    int height;
    TextView title;
    EditText txtRecipientInfo;
    EditText txtMessageBody;
    Button btnSend;
    ArrayList<Contact> contacts;
    String currentUser;
    DisplayMetrics dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_text);
        dm = new DisplayMetrics();
        GetDisplayMetrics(dm);
        UnpackBundle();
        SetupViews();
    }

    /**
     * This function unpacks the bundle containing the username of the currently logged in user and contacts list
     */
    private void UnpackBundle() {
        Bundle b = getIntent().getExtras();
        if(b == null)
            throw new Error("null bundle found");
        if(b.getString(FROM_KEY,"").equals("HomeActivity")){
            currentUser = b.getString(USER_NAME_KEY,"");
            if(currentUser.equals(""))
                throw new Error("Invalid user logged in");
            contacts = b.getParcelableArrayList(CONTACTS_LIST_KEY);
            if((contacts != null ? contacts.size() : 0) == 0){
                InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("Please ensure you have your nurse in contacts",this);
                fragment.show(getFragmentManager(),"dia");
            }
        }
        else
            throw new Error("Invalid bundle Found");
    }

    /**
     * This function sets up the user interface for the SendTextActivity
     */
    private void SetupViews() {
        title = (TextView) findViewById(R.id.sendTextTitle);
        txtRecipientInfo = (EditText) findViewById(R.id.txtRecipientInfo);
        txtMessageBody = (EditText) findViewById(R.id.txtMessageBody);
        btnSend = (Button) findViewById(R.id.btnSendTextMessage);
        SetupTitleLayout();
        SetupRecipientsNameLayout();
        SetupMessageBodyLayout();
        SetupSendButtonLayout();
        SetupSendButtonOnClick();
        GetNurseContact();
    }

    /**
     *This function gets the contact with the name specified in the users nurse name.
     * @return the contact with the name specified in the users nurse name.
     */
    private Contact GetNurseContact() {
        UserDetails users = ReadUsers();
        assert users != null;
        for(User u : users.getUserinfo()){
            for(Contact c : contacts){
                if(c.getName().equals(u.getNurseName())){
                    txtRecipientInfo.setText(c.getName());
                    return c;
                }
            }
        }
        return null;
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
     * This function gets a contact from a name or phone number
     * @return the contact with this name or number or null if no contact exists
     */
    private Contact GetContactFromNameOrNumber() {
        for(Contact c : contacts){
            if(c.getName().equals(txtRecipientInfo.getText().toString()) ||
                    c.getPhoneNumber().equals(txtRecipientInfo.getText().toString())){
                return c;
            }
        }
        return null;
    }

    /**
     * On Click listener for the send message button
     */
    private void SetupSendButtonOnClick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("send message", "Send message button clicked");
                Contact reciever = GetContactFromNameOrNumber();
                if (reciever == null) {
                    ShowInvalidContactDialog();
                    return;
                }
                SmsManager smsManager = SmsManager.getDefault();
                String phoneNumber = reciever.getPhoneNumber();
                String messageBody = txtMessageBody.getText().toString();
                Log.e("message", messageBody);
                smsManager.sendTextMessage(phoneNumber, null, messageBody, null, null);
                Toast.makeText(getBaseContext(), "Message Successfully Sent To " + reciever.getName(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /**
     * This function shows a dialog informing the user that they have entered invalid contact information
     */
    private void ShowInvalidContactDialog() {
        InvalidInputDialogFragment fragment = new InvalidInputDialogFragment("Invalid contact name or number " +
                txtRecipientInfo.getText().toString(), this);
        fragment.show(getFragmentManager(),"dia");
    }

    /**
     * This function sets up the layout for the send message button
     */
    private void SetupSendButtonLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.80);
        btnSend.setGravity(Gravity.CENTER);
        btnSend.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the message body edit text
     */
    private void SetupMessageBodyLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = (int) (height * 0.60);
        params.topMargin = (int) (height * 0.20);
        txtMessageBody.setGravity(Gravity.START);
        txtMessageBody.setBackground(new ListViewBorder(new RectShape()));
        txtMessageBody.setLayoutParams(params);
    }
    /**
     * This function sets up the layout for the recipients name edit text
     */
    private void SetupRecipientsNameLayout() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = (int) (height * 0.10);
        txtRecipientInfo.setGravity(Gravity.CENTER);
        txtRecipientInfo.setLayoutParams(params);
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
