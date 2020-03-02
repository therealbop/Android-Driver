package com.karry.pojo.Signup;

import java.io.Serializable;

/**
 * Created by embed on 14/3/18.
 */

public class ColorData implements Serializable {

    private String color;

    private boolean selected;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
