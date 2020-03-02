package com.karry.pojo.Signup;

import java.io.Serializable;

public class Gender implements Serializable {

    private String gender;

    private boolean selected;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
