package com.needham.thomas.medicare.root.Classes;

import java.io.Serializable;

/**
 * Created by Thomas Needham on 01/04/2016.
 * This class represents an end user within the app
 */
public class User implements Serializable{
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private String address1;
    private String address2;
    private String phoneNumber;
    private String nurseName;
    private float fontSize;
    private int fontColour;
    private int backgroundColour;

    public User(String userName, String password, String firstName, String lastName, int age,
                String address1, String address2, String phoneNumber, String nurseName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address1 = address1;
        this.address2 = address2;
        this.phoneNumber = phoneNumber;
        this.nurseName = nurseName;
        this.fontColour = 0;
        this.fontSize = 0.0f;
        this.backgroundColour = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColour() {
        return fontColour;
    }

    public void setFontColour(int fontColour) {
        this.fontColour = fontColour;
    }

    public int getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(int backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    @Override
    public String toString() {
        return "Username " + userName + "\n" +
                "First Name " + firstName + "\n" +
                "Last Name " + lastName + "\n" +
                "Age " + age + "\n" +
                "Address 1" + address1 + "\n" +
                "Address 2 " + address2 + "\n" +
                "Phone Number " + phoneNumber + "\n" +
                "Nurse Name " + nurseName + "\n";
    }
}
