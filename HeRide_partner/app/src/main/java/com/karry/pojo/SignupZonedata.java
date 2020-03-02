package com.karry.pojo;

import java.io.Serializable;

/**
 * Created by embed on 16/5/17.
 */

public class SignupZonedata implements Serializable {

    private String Name;

    private String id;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
