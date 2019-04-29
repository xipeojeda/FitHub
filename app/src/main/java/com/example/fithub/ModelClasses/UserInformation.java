package com.example.fithub.ModelClasses;

import com.firebase.ui.auth.data.model.User;

public class UserInformation
{
    String fName, lName, emailAddress, birthDate;
    int age;

    public UserInformation()
    {

    }
    public UserInformation(String fName, String lName, String emailAddress,String birthDate, int age)
    {
        this.fName = fName;
        this.lName = lName;
        this.emailAddress = emailAddress;
        this.birthDate = birthDate;
        this.age = age;
    }

    public void setBirthDate(String birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public void setfName(String fName)
    {
        this.fName = fName;
    }

    public void setlName(String lName)
    {
        this.lName = lName;
    }

    public String getlName()
    {
        return lName;
    }

    public String getfName()
    {
        return fName;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public String getBirthDate()
    {
        return birthDate;
    }

    public int getAge()
    {
        return age;
    }
}
