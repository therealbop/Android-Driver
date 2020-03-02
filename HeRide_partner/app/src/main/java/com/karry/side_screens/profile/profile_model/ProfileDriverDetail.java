package com.karry.side_screens.profile.profile_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karry.pojo.Signup.PreferencesList;

import java.util.ArrayList;

public class ProfileDriverDetail
{
    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("profilePic")
    @Expose
    private String profilePic;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("driverPreferencesArr")
    @Expose
    private ArrayList<PreferencesList> driverPreferencesArr;


    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getProfilePic ()
    {
        return profilePic;
    }

    public void setProfilePic (String profilePic)
    {
        this.profilePic = profilePic;
    }

    public String getCountryCode ()
    {
        return countryCode;
    }

    public void setCountryCode (String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public ArrayList<PreferencesList> getDriverPreferencesArr() {
        return driverPreferencesArr;
    }

    public void setDriverPreferencesArr(ArrayList<PreferencesList> driverPreferencesArr) {
        this.driverPreferencesArr = driverPreferencesArr;
    }
}
