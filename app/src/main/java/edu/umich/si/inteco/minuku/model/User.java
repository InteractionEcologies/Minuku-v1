package edu.umich.si.inteco.minuku.model;

/**
 * Created by tsung on 2017/2/10.
 */

public class User {

    private String userName;
    // Img number refrence in UserIconReference.class
    private String imgNumber;
    private String ifSelected;
    private String userAge;

    public User() {
        imgNumber = "24";
        ifSelected = "0";
    }

    public User(String userName, String userAge, String imgNumber, String ifSelected) {
        this.userName = userName;
        this.imgNumber = imgNumber;
        this.ifSelected = ifSelected;
        this.userAge = userAge;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return this.userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getImgNumber() {
        return this.imgNumber;
    }

    public void setImgNumber(String imgNumber) {
        this.imgNumber = imgNumber;
    }

    public String getIfSelected() {
        return this.ifSelected;
    }

    public void setIfSelected(boolean ifSelected) {
        if (ifSelected) {
            this.ifSelected = "1";
        } else {
            this.ifSelected = "0";
        }
    }
}
