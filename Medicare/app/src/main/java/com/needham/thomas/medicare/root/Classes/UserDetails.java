package com.needham.thomas.medicare.root.Classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Thomas Needham on 01/04/2016.
 * This singleton class represents all of the registered users within the app
 */
public class UserDetails implements Serializable {
    private ArrayList<User> userInfo;
    private static UserDetails Instance;

    /**
     * constructs a new instance of userInfo
     * @param userInfo the list of userInfo to use
     */
    private UserDetails(ArrayList<User> userInfo)
    {
        if (this.userInfo == null && userInfo == null)
        {
            this.setUserinfo(new ArrayList<User>());
        }
        else if(userInfo != null)
        {
            this.setUserinfo(userInfo);
        }
        Instance = this;

    }

    /**
     * get the currently active instance of this class or creates one if it does not exists
     * @return the currently active instance of this class
     */
    public static UserDetails GetInstance()
    {
        if(Instance == null)
        {
            Instance = new UserDetails(null);
            return Instance;
        }
        else
        {
            return Instance;
        }
    }

    /**
     * et the currently active instance of this class or creates one if it does not exists
     * @param userInfo the list of userInfo to use to create the new instance
     * @return the currently active instance of this class
     */
    public static UserDetails GetInstance(ArrayList<User> userInfo)
    {
        if(Instance == null)
        {
            Instance = new UserDetails(userInfo);
            return Instance;
        }
        else
        {
            return Instance;
        }
    }


    public ArrayList<User> getUserinfo() {
        return userInfo;
    }

    public void setUserinfo(ArrayList<User> userinfo) {
        this.userInfo = userinfo;
    }

    /**
     * Add a new User to the current list of User
     * @param User the User to add
     * @return the added User
     */
    public User addUser(User User)
    {
        this.userInfo.add(User);
        return User;
    }
}
